package eu.ohoos.currency.data.api.model

import androidx.collection.ArrayMap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CurrencyNetworkData(
    @Expose @SerializedName("success") val success: Boolean,
    @Expose @SerializedName("base") val base: String,
    @Expose @SerializedName("rates") val rates: ArrayMap<String, BigDecimal>
)
