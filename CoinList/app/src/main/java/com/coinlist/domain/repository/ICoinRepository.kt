package com.coinlist.domain.repository

import com.coinlist.data.remote.dto.CoinPriceListDto
import com.coinlist.data.remote.dto.TradingPairListDto

interface ICoinRepository {

    suspend fun getTradingPairList(): TradingPairListDto

    suspend fun getPairPriceList(): CoinPriceListDto
}