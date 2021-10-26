package com.coinlist.domain.di

import com.coinlist.CoinListActivity
import com.coinlist.domain.di.viewmodel.ViewModelFactoryModule
import com.coinlist.domain.di.viewmodel.ViewModelModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [
            FragmentBuildersModule::class,
            ViewModelModule::class,
            ViewModelFactoryModule::class
        ]
    )
    abstract fun contributeCoinListActivity(): CoinListActivity
}