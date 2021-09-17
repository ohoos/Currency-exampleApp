package eu.ohoos.currency.ui.extensions

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> MutableStateFlow<T>.update(fcn: (T) -> T) {
    value = (value.let(fcn))
}

fun <T> Flow<T>.collectScoped(scope: CoroutineScope, action: suspend (value: T) -> Unit): Job =
    scope.launch { collect(action) }

fun <T> Flow<T>.collectToLiveData(scope: CoroutineScope, liveData: MutableLiveData<T>): Job =
    scope.launch {
        collect {
            liveData.value = it
        }
    }