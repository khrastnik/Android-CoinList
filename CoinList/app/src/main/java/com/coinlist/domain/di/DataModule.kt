package com.coinlist.domain.di

import com.coinlist.data.repository.CoinRepositoryImpl
import com.coinlist.domain.repository.ICoinRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindCoinRepository(repo: CoinRepositoryImpl): ICoinRepository
}
