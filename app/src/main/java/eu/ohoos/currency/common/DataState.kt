package eu.ohoos.currency.common

sealed class DataState<out R> {

    data class Success<out T>(val data: T) : DataState<T>()

    data class Error(val throwable: Throwable) : DataState<Nothing>()

    object Loading : DataState<Nothing>()
}