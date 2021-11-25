package com.coinlist.di

import com.coinlist.ui.CoinListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeCoinListFragment(): CoinListFragment
}
