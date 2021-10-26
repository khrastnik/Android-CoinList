package com.coinlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coinlist.domain.usecase.BaseUseCase
import com.coinlist.domain.usecase.GetPairWithPriceUseCase

class ViewModelFactory(private val baseUseCase: BaseUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinListViewModel::class.java)) {
            return CoinListViewModel(baseUseCase as GetPairWithPriceUseCase) as T
        }
        throw IllegalAccessException("Unknown viewModel class name")
    }
}