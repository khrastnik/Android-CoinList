package com.coinlist.ui

import androidx.recyclerview.widget.DiffUtil
import com.coinlist.domain.model.CoinModel


class CoinListDiffCallback(
    private val newItems: List<CoinModel>,
    private val oldItems: List<CoinModel>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].name == newItems[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }
}