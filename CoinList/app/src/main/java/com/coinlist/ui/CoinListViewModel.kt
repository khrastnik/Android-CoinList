package com.coinlist.ui

import androidx.lifecycle.viewModelScope
import com.coinlist.common.Resource
import com.coinlist.domain.model.CoinModel
import com.coinlist.domain.usecase.GetPairWithPriceUseCase
import com.coinlist.ui.base.BaseViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CoinListViewModel @Inject constructor(
    private val getPairWithPriceUseCase: GetPairWithPriceUseCase
) : BaseViewModel(getPairWithPriceUseCase) {

    private val _pairPriceList =
        MutableStateFlow<Resource<List<CoinModel>>>(Resource.Loading())
    val pairPriceList :StateFlow<Resource<List<CoinModel>>> = _pairPriceList

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
                    _pairPriceList.emit(it)
                }
            }
        }
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
