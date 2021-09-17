package eu.ohoos.currency.ui.common

import androidx.recyclerview.widget.DiffUtil
import eu.ohoos.currency.data.db.entity.CurrencyRate

class CurrencyRateDiffCallback : DiffUtil.ItemCallback<CurrencyRate>() {
    override fun areItemsTheSame(oldItem: CurrencyRate, newItem: CurrencyRate): Boolean {
        return oldItem.symbol == newItem.symbol
    }

    override fun areContentsTheSame(
        oldItem: CurrencyRate,
        newItem: CurrencyRate
    ): Boolean {
        return oldItem == newItem
    }
}