package com.coinlist.ui

import androidx.lifecycle.viewModelScope
import com.coinlist.domain.model.CoinModel
import com.coinlist.domain.usecase.GetPairWithPriceUseCase
import com.coinlist.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoinListViewModel @Inject constructor(
    private val getPairWithPriceUseCase: GetPairWithPriceUseCase
) : BaseViewModel(getPairWithPriceUseCase) {

    private val _pairPriceList = MutableStateFlow<CoinListUiState>(CoinListUiState.Loading)
    var pairPriceList: StateFlow<CoinListUiState> = _pairPriceList

    private var listItems: List<CoinModel>? = mutableListOf()
    private var counterItems: MutableList<String> = mutableListOf()
    private var selectedCounterName: String? = null
    private var tickerJob: Job? = null

    fun getPairWithPrice() {
        if (tickerJob == null || !tickerJob!!.isActive) {
            tickerJob = viewModelScope.launch {
                getPairWithPriceUseCase()
                    .catch {
                        _pairPriceList.value = CoinListUiState.Error(it.fillInStackTrace())
                    }
                    .collect {
                        setListItems(it)
                        setPairCounters()
                        _pairPriceList.emit(CoinListUiState.Success(it))
                    }
            }
        }
    }

    private fun setListItems(listItems: List<CoinModel>) {
        this.listItems = listItems
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

    // Represents different states for the CoinListUiState screen
    sealed class CoinListUiState {
        object Loading : CoinListUiState()
        data class Success(var items: List<CoinModel>) : CoinListUiState()
        data class Error(var exception: Throwable) : CoinListUiState()
    }
}
