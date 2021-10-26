package com.coinlist.data

import com.coinlist.data.remote.dto.CoinPriceListDto
import com.coinlist.data.remote.dto.TradingPairListDto
import retrofit2.http.GET

interface IApiService {

    @GET("v1/pairs")
    suspend fun getTradingPairList(): TradingPairListDto

    @GET("v1/tickers")
    suspend fun getPairPriceList(): CoinPriceListDto
}