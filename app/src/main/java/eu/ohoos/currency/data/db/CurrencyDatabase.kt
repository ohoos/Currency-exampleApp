package eu.ohoos.currency.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.ohoos.currency.data.db.converter.BigDecimalTypeConverter
import eu.ohoos.currency.data.db.dao.CurrencyRateDao
import eu.ohoos.currency.data.db.entity.CurrencyRate
import eu.ohoos.currency.data.db.entity.FavoriteCurrencyCacheEntity

@Database(
    entities = [CurrencyRate::class, FavoriteCurrencyCacheEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(BigDecimalTypeConverter::class)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyRateDao(): CurrencyRateDao

    companion object {
        const val DATABASE_NAME: String = "currency_db"
    }
}