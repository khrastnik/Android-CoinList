package com.coinlist.data.remote.dto

import com.coinlist.domain.model.CoinModel
import com.google.gson.annotations.SerializedName


data class TradingPairDto(

    @SerializedName("base_decimals")
    var baseDecimals: Int,
    @SerializedName("counter_decimals")
    var counterDecimals: Int,
    var base: String,
    @SerializedName("minimum_order")
    var minimumOrder: String,
    var name: String,
    var pair: String,
    var counter: String,
    var description: String,
)

fun TradingPairDto.mapToCoinModel(): CoinModel {
    return CoinModel(
        base = base,
        minimumOrder = minimumOrder,
        name = name,
        pair = pair,
        counter = counter,
        ask = ""
    )
}

fun TradingPairDto.mapToCounterString(): String {
    return counter
}