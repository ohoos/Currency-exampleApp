package eu.ohoos.currency.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteCurrencyCacheEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "symbol") val symbol: String
)