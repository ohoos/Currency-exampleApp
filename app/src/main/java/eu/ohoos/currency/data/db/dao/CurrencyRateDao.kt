package eu.ohoos.currency.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.ohoos.currency.data.db.entity.CurrencyRate
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencyRateModel: CurrencyRate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<CurrencyRate>)

    @Query("SELECT * FROM currency ORDER BY name")
    fun getAll(): List<CurrencyRate>

    @Query("SELECT * FROM currency WHERE isFavorite = 1 ORDER BY name")
    fun getAllFavorite(): List<CurrencyRate>

    @Query("SELECT * FROM currency ORDER BY name")
    fun getAllFlow(): Flow<List<CurrencyRate>>

    @Query("SELECT * FROM currency WHERE isFavorite = 1 ORDER BY name")
    fun getAllFavoriteFlow(): Flow<List<CurrencyRate>>

}