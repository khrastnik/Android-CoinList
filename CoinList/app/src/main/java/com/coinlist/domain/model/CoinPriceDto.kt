package com.coinlist.domain.model

data class CoinPriceDto(
    var last24: String,
    var timestamp: String,
    var bid: String,
    var high: String,
    var ask: String,
    var pair: String,
    var open: String,
    var volume: String,
    var last: String,
    var vwap: String,
    var low: String
)

fun CoinPriceDto.mapToCoinPriceModel(): CoinPriceModel {
    return CoinPriceModel(
        pair = pair,
        ask = ask
    )
}
