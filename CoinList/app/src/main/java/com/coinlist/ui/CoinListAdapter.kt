package com.coinlist.ui


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.coinlist.R
import com.coinlist.domain.model.CoinModel


class CoinListAdapter(private var data: ArrayList<CoinModel>) :
    RecyclerView.Adapter<CoinListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.coin_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.name
        holder.minimumOrder.text = item.minimumOrder
        holder.price.text = String.format("%s %s", item.ask, item.counter)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setItems(items: List<CoinModel>) {
        val diff = DiffUtil.calculateDiff(CoinListDiffCallback(data, items))
        data.clear()
        data.addAll(items)
        diff.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.tvName)
        var minimumOrder: TextView = itemView.findViewById(R.id.tvMinimumOrderValue)
        var price: TextView = itemView.findViewById(R.id.tvPrice)
    }
}