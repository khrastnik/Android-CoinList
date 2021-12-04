package com.coinlist.domain.usecase

import com.coinlist.domain.model.CoinModel
import com.coinlist.domain.model.mapToCoinModel
import com.coinlist.domain.repository.ICoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTradingPairUseCase @Inject constructor(private val repository: ICoinRepository) :
    BaseUseCase() {
    override operator fun invoke(): Flow<List<CoinModel>> = flow {
        emit(repository.getTradingPairList().data.map { it.mapToCoinModel() })
    }
}
