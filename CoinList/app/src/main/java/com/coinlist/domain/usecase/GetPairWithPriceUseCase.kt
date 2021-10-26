package com.coinlist.domain.usecase

import com.coinlist.common.Resource
import com.coinlist.data.remote.dto.mapToCoinModel
import com.coinlist.data.remote.dto.mapToCoinPriceModel
import com.coinlist.domain.model.CoinModel
import com.coinlist.domain.repository.ICoinRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPairWithPriceUseCase @Inject constructor(private val repository: ICoinRepository) :
    BaseUseCase() {

    private val refreshIntervalMs: Long = 3000

    override operator fun invoke(): Flow<Resource<List<CoinModel>>> = flow {
        try {
            val tradingPairs = repository.getTradingPairList().data.map { it.mapToCoinModel() }

            emit(Resource.Success(tradingPairs))

            while (true) {
                val pairPriceList =
                    repository.getPairPriceList().data.map { it.mapToCoinPriceModel() }

                val resultWithPrices = tradingPairs.mapNotNull { coinPairItem ->
                    val coinPriceItem = pairPriceList.find { it.pair == coinPairItem.pair }
                    if (coinPriceItem == null) {
                        null
                    } else {
                        CoinModel(
                            coinPairItem.base, coinPairItem.minimumOrder, coinPairItem.name,
                            coinPairItem.pair, coinPairItem.counter, coinPriceItem.ask
                        )
                    }
                }
                emit(Resource.Success(resultWithPrices))
                delay(refreshIntervalMs)
            }
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<CoinModel>>(
                    e.localizedMessage ?: "An http error occurred", null
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.NoInternet<List<CoinModel>>(
                    "Couldn't reach server. Check your internet connection.",
                    null
                )
            )
        } catch (e: Exception) {
            emit(
                Resource.Error<List<CoinModel>>(
                    e.localizedMessage ?: "An unexpected error occurred", null
                )
            )
        }
    }
}