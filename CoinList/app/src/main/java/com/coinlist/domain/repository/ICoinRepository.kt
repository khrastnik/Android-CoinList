package com.coinlist.domain.repository

import com.coinlist.domain.model.CoinPriceListDto
import com.coinlist.domain.model.TradingPairListDto

interface ICoinRepository {

    suspend fun getTradingPairList(): TradingPairListDto

    suspend fun getPairPriceList(): CoinPriceListDto
}
