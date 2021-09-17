package eu.ohoos.currency.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.ohoos.currency.data.api.service.CurrencyService
import eu.ohoos.currency.data.db.dao.CurrencyRateDao
import eu.ohoos.currency.data.repository.CurrencyRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCurrencyRepository(
        currencyService: CurrencyService,
        currencyRateDao: CurrencyRateDao
    ): CurrencyRepository =
        CurrencyRepository(
            currencyService,
            currencyRateDao
        )
}