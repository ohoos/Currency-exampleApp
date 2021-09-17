package eu.ohoos.currency.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.ohoos.currency.data.db.CurrencyDatabase
import eu.ohoos.currency.data.db.dao.CurrencyRateDao
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideCurrencyDb(@ApplicationContext context: Context): CurrencyDatabase =
        Room
            .databaseBuilder(
                context,
                CurrencyDatabase::class.java,
                CurrencyDatabase.DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideCurrencyRateDao(currencyDatabase: CurrencyDatabase): CurrencyRateDao =
        currencyDatabase.currencyRateDao()

}