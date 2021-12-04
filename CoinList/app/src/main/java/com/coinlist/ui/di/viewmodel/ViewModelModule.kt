package com.coinlist.ui.di.viewmodel

import androidx.lifecycle.ViewModel
import com.coinlist.ui.CoinListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CoinListViewModel::class)
    protected abstract fun bindCoinListViewModel(viewModel: CoinListViewModel): ViewModel
}
