package com.coinlist.domain.usecase

import com.coinlist.common.Resource
import com.coinlist.domain.model.mapToCoinModel
import com.coinlist.domain.model.mapToCoinPriceModel
import com.coinlist.domain.model.CoinModel
import com.coinlist.domain.repository.ICoinRepository
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetPairWithPriceUseCase @Inject constructor(private val repository: ICoinRepository) :
    BaseUseCase() {

    private val refreshIntervalMs: Long = 3000

    override operator fun invoke(): Flow<Resource<List<CoinModel>>> = flow {
        try {
            val tradingPairList = repository.getTradingPairList().data.map { it.mapToCoinModel() }

            emit(Resource.Success(tradingPairList))

            while (true) {
                val pairPriceList =
                    repository.getPairPriceList().data.map { it.mapToCoinPriceModel() }
                val resultWithPrices = tradingPairList.mapNotNull { coinPairItem ->
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
            emit(Resource.Error<List<CoinModel>>(e.localizedMessage ?: "An http error occurred", null))
        } catch (e: IOException) {
            emit(Resource.NoInternet<List<CoinModel>>(e.localizedMessage ?: "Couldn't reach server. Check your internet connection.", null))
        } catch (e: Exception) {
            emit(Resource.Error<List<CoinModel>>(e.localizedMessage ?: "An unexpected error occurred", null))
        }
    }
}
