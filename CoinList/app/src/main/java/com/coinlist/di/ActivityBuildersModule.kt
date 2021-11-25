package com.coinlist.di

import com.coinlist.di.viewmodel.ViewModelFactoryModule
import com.coinlist.di.viewmodel.ViewModelModule
import com.coinlist.ui.CoinListActivity
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
