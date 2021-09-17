package eu.ohoos.currency.data.api.model

import androidx.collection.ArrayMap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CurrencySymbolsNetworkData(
    @Expose @SerializedName("success") val success: Boolean,
    @Expose @SerializedName("symbols") val symbols: ArrayMap<String, String>
)
