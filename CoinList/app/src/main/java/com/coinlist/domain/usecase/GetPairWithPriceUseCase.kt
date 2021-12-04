package com.coinlist.domain.usecase

import com.coinlist.domain.model.CoinModel
import com.coinlist.domain.model.mapToCoinPriceModel
import com.coinlist.domain.repository.ICoinRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPairWithPriceUseCase @Inject constructor(private val repository: ICoinRepository) :
    BaseUseCase() {

    private val refreshIntervalMs: Long = 3000

    override operator fun invoke(): Flow<List<CoinModel>> = flow {
        val tradingPairUseCase = GetTradingPairUseCase(repository)
        tradingPairUseCase()
            .collect {
                val tradingPairList = it
                emit(tradingPairList)
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
                    emit(resultWithPrices)
                    delay(refreshIntervalMs)
                }
            }
    }
}
