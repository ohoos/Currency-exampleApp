package eu.ohoos.currency.ui.currencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.ohoos.currency.R
import eu.ohoos.currency.data.db.entity.CurrencyRate
import eu.ohoos.currency.ui.common.CurrencyRateDiffCallback

class CurrencyRateAdapter
    : ListAdapter<CurrencyRate, RecyclerView.ViewHolder>(CurrencyRateDiffCallback()) {

    class CurrencyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainLayout: ConstraintLayout = view.findViewById(R.id.mainLayout)
        val txtName: TextView = view.findViewById(R.id.txtName)
        val txtSymbol: TextView = view.findViewById(R.id.txtSymbol)
        val txtRate: TextView = view.findViewById(R.id.txtRate)
        val txtResult: TextView = view.findViewById(R.id.txtResult)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        CurrencyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_currency_rate_result_view, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is CurrencyViewHolder -> {
                with(holder) {
                    txtName.text = item.name
                    txtSymbol.text = item.symbol
                    txtRate.text = item.getRateString()
                    txtResult.text = item.getCalculatedAmountString()
                }
            }
        }
    }
}