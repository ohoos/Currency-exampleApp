package eu.ohoos.currency.ui.favorite

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.ohoos.currency.R
import eu.ohoos.currency.data.db.entity.CurrencyRate
import eu.ohoos.currency.ui.common.CurrencyRateDiffCallback

class FavoriteAdapter(
    private val onFavoriteClickListener: ((CurrencyRate) -> Unit)? = null
) : ListAdapter<CurrencyRate, RecyclerView.ViewHolder>(CurrencyRateDiffCallback()) {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainLayout: ConstraintLayout = view.findViewById(R.id.mainLayout)
        val imgFavorite: ImageView = view.findViewById(R.id.imgFavorite)
        val txtName: TextView = view.findViewById(R.id.txtName)
        val txtRate: TextView = view.findViewById(R.id.txtRate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        FavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_currency_rate_view, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is FavoriteViewHolder -> {
                with(holder) {
                    txtName.text = item.name
                    txtRate.text = item.getRateString()

                    imgFavorite.setImageDrawable(
                        getFavoriteIconDrawable(
                            holder.itemView.context,
                            item.isFavorite
                        )
                    )

                    mainLayout.setOnClickListener {
                        onFavoriteClickListener?.invoke(item)
                    }
                }
            }
        }
    }

    private fun getFavoriteIconDrawable(context: Context, isFavorite: Boolean): Drawable? =
        ContextCompat.getDrawable(
            context, when (isFavorite) {
                true -> R.drawable.ic_star
                else -> R.drawable.ic_star_outline
            }
        )
}