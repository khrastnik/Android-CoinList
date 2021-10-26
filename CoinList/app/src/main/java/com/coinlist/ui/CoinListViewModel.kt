package com.coinlist.ui

import androidx.lifecycle.viewModelScope
import com.coinlist.common.Resource
import com.coinlist.domain.model.CoinModel
import com.coinlist.domain.usecase.GetPairWithPriceUseCase
import com.coinlist.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoinListViewModel @Inject constructor(
    private val getPairWithPriceUseCase: GetPairWithPriceUseCase
) : BaseViewModel(getPairWithPriceUseCase) {

    private val pairPriceList =
        MutableStateFlow<Resource<List<CoinModel>>>(Resource.Loading())

    private var listItems: List<CoinModel>? = emptyList()
    private var counterItems: MutableList<String> = mutableListOf()
    private var selectedCounterName: String? = null
    private var tickerJob: Job? = null

    fun getPairWithPrice() {
        if (tickerJob == null || !tickerJob!!.isActive) {
            tickerJob = viewModelScope.launch {
                getPairWithPriceUseCase().collect {
                    if (it is Resource.Success) {
                        listItems = it.data
                        setPairCounters()
                    }
                    pairPriceList.emit(it)
                }
            }
        }
    }

    fun getPairPriceList(): StateFlow<Resource<List<CoinModel>>> {
        return pairPriceList
    }

    private fun setPairCounters() {
        if (listItems != null && counterItems.isEmpty()) {
            counterItems = listItems!!.map { it.counter }.distinct().toMutableList()
        }
    }

    fun setSelectedCounterName(counterName: String?) {
        this.selectedCounterName = counterName
    }

    fun getCounterItems(): MutableList<String> {
        return counterItems
    }

    fun stopTickerJob() {
        viewModelScope.launch {
            tickerJob?.cancelAndJoin()
            tickerJob = null
        }
    }

    fun getListItems(): List<CoinModel> {
        var result = emptyList<CoinModel>()
        if (!listItems.isNullOrEmpty() && !selectedCounterName.isNullOrEmpty()) {
            result = listItems!!.filter { it.counter == selectedCounterName }
        } else if (!listItems.isNullOrEmpty()) {
            result = listItems!!
        }
        return result
    }
}
