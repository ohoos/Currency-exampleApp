package eu.ohoos.currency.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.ohoos.currency.common.DataState
import eu.ohoos.currency.data.db.entity.CurrencyRate
import eu.ohoos.currency.data.repository.CurrencyRepository
import eu.ohoos.currency.ui.extensions.collectScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel
@Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _currencyRatesFlow: MutableSharedFlow<DataState<List<CurrencyRate>>> =
        MutableSharedFlow()
    val sortedCurrencyRates: LiveData<DataState<List<CurrencyRate>>> =
        _currencyRatesFlow.asLiveData()

    private val _filterQueryFlow: MutableStateFlow<String> = MutableStateFlow("")

    init {
        refresh()
    }

    fun addOrRemoveFavoriteCurrency(currency: CurrencyRate) =
        when (currency.isFavorite) {
            true -> currencyRepository.removeFavorite(currency)
            else -> currencyRepository.addFavorite(currency)
        }

    fun refresh() {
        combine(
            currencyRepository.getAllCurrencies(true),
            _filterQueryFlow
        ) { currencyRates, filterQuery ->
            Pair(currencyRates, filterQuery)
        }.onEach {
            _currencyRatesFlow.emit(DataState.Loading)
        }.collectScoped(viewModelScope) { result ->
            _currencyRatesFlow.emit(
                when (result.first) {
                    is DataState.Success -> {
                        DataState.Success(
                            (result.first as DataState.Success).data.sortedByDescending {
                                it.isFavorite
                            }.filter { currencyRate ->
                                currencyRate.symbol.lowercase()
                                    .contains(result.second.lowercase())
                                        || currencyRate.name.lowercase()
                                    .contains(result.second.lowercase())
                            })
                    }
                    else -> result.first
                }
            )
        }
    }

    fun filterCurrencies(query: String) {
        _filterQueryFlow.value = query
    }
}