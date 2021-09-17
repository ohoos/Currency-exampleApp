package eu.ohoos.currency.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import eu.ohoos.currency.data.db.converter.BigDecimalTypeConverter
import java.math.BigDecimal
import java.math.RoundingMode

@Entity(tableName = "currency")
data class CurrencyRate(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "baseRateSymbol") val baseRateSymbol: String,
    @TypeConverters(BigDecimalTypeConverter::class)
    @ColumnInfo(name = "rate") val rate: BigDecimal,
    val calculatedAmount: BigDecimal = BigDecimal.ZERO,
    val isFavorite: Boolean = false
) {
    fun getRateString() =
        "1 $baseRateSymbol ~ ${rate.setScale(2, RoundingMode.HALF_UP)} $symbol"

    fun getCalculatedAmountString() =
        "${calculatedAmount.setScale(2, RoundingMode.HALF_UP)} $symbol"
}