package eu.ohoos.currency.data.repository

import eu.ohoos.currency.common.DataState
import eu.ohoos.currency.common.constants.Constants
import eu.ohoos.currency.data.api.model.CurrencyNetworkData
import eu.ohoos.currency.data.api.model.CurrencySymbolsNetworkData
import eu.ohoos.currency.data.api.service.CurrencyService
import eu.ohoos.currency.data.db.dao.CurrencyRateDao
import eu.ohoos.currency.data.db.entity.CurrencyRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrencyRepository
@Inject
constructor(
    private val currencyService: CurrencyService,
    private val currencyRateDao: CurrencyRateDao
) {

    private fun getCurrencyRateListFlow(
        refresh: Boolean,
        onlyFavorite: Boolean
    ): Flow<DataState<List<CurrencyRate>>> = flow {
        if (refresh) {
            try {
                val ratesNetworkData = currencyService.getLatest(Constants.API_KEY)
                val symbolsNetworkData = currencyService.getSymbols(Constants.API_KEY)

                if (!ratesNetworkData.isSuccessful || ratesNetworkData.body() == null ||
                    !symbolsNetworkData.isSuccessful || symbolsNetworkData.body() == null
                ) {
                    emit(DataState.Error(Throwable("Network error")))
                } else {
                    val currencyRateList =
                        margeNetworkData(ratesNetworkData.body()!!, symbolsNetworkData.body()!!)
                    val favoriteList = currencyRateDao.getAllFavorite()
                    val list = currencyRateList.map {
                        val favoriteSymbols =
                            favoriteList.map { favoriteItem -> favoriteItem.symbol }
                        it.copy(isFavorite = favoriteSymbols.contains(it.symbol))
                    }
                    currencyRateDao.insertAll(list)

                    if (onlyFavorite)
                        emitAll(currencyRateDao.getAllFavoriteFlow().map { DataState.Success(it) })
                    else
                        emitAll(currencyRateDao.getAllFlow().map { DataState.Success(it) })

                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                emit(DataState.Error(ex))
            }
        } else {
            if (onlyFavorite)
                emitAll(currencyRateDao.getAllFavoriteFlow().map { DataState.Success(it) })
            else
                emitAll(currencyRateDao.getAllFlow().map { DataState.Success(it) })
        }
    }.flowOn(Dispatchers.IO)

    fun getFavoritesFlow(refresh: Boolean): Flow<DataState<List<CurrencyRate>>> =
        getCurrencyRateListFlow(refresh, true)

    fun getAllCurrencies(refresh: Boolean): Flow<DataState<List<CurrencyRate>>> =
        getCurrencyRateListFlow(refresh, false)

    private fun margeNetworkData(
        ratesData: CurrencyNetworkData,
        symbolsData: CurrencySymbolsNetworkData
    ): List<CurrencyRate> =
        ratesData.rates.mapNotNull { rateEntry ->
            symbolsData.symbols[rateEntry.key]?.let { name ->
                CurrencyRate(
                    baseRateSymbol = ratesData.base,
                    rate = rateEntry.value,
                    symbol = rateEntry.key,
                    name = name
                )
            }
        }.toList()

    fun addFavorite(currencyRate: CurrencyRate) = CoroutineScope(Dispatchers.IO).launch {
        currencyRateDao.insert(currencyRate.copy(isFavorite = true))
    }

    fun removeFavorite(currencyRate: CurrencyRate) = CoroutineScope(Dispatchers.IO).launch {
        currencyRateDao.insert(currencyRate.copy(isFavorite = false))
    }
}