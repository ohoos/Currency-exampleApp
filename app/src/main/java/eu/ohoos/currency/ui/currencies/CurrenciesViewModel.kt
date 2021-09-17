package eu.ohoos.currency.ui.currencies

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
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel
@Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _currencyRatesFlow: MutableSharedFlow<DataState<List<CurrencyRate>>> =
        MutableSharedFlow()
    val currencyRates: LiveData<DataState<List<CurrencyRate>>> = _currencyRatesFlow.asLiveData()

    private val _amountFlow: MutableStateFlow<BigDecimal> = MutableStateFlow(BigDecimal.ZERO)

    init {
        refresh()
    }

    fun refresh() {
        combine(currencyRepository.getFavoritesFlow(true), _amountFlow) { currencyRates, amount ->
            Pair(currencyRates, amount)
        }.onEach {
            _currencyRatesFlow.emit(DataState.Loading)
        }.collectScoped(viewModelScope) { result ->
            _currencyRatesFlow.emit(
                when (result.first) {
                    is DataState.Success -> {
                        DataState.Success((result.first as DataState.Success).data.map { currencyRate ->
                            currencyRate.copy(calculatedAmount = currencyRate.rate.multiply(result.second))
                        })
                    }
                    else -> result.first
                }
            )
        }
    }

    fun calculateAmount(amountText: String) {
        _amountFlow.value = if (amountText.isEmpty()) BigDecimal.ZERO else BigDecimal(amountText)
    }
}