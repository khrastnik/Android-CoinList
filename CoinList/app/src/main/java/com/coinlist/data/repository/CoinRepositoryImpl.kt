package com.coinlist.data.repository

import com.coinlist.data.IApiService
import com.coinlist.domain.model.CoinPriceListDto
import com.coinlist.domain.model.TradingPairListDto
import com.coinlist.domain.repository.ICoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(private val remoteDataSource: IApiService) :
    ICoinRepository {

    override suspend fun getTradingPairList(): TradingPairListDto {
        return remoteDataSource.getTradingPairList()
    }

    override suspend fun getPairPriceList(): CoinPriceListDto {
        return remoteDataSource.getPairPriceList()
    }
}
