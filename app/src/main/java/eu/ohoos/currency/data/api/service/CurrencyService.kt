package eu.ohoos.currency.data.api.service

import eu.ohoos.currency.data.api.model.CurrencyNetworkData
import eu.ohoos.currency.data.api.model.CurrencySymbolsNetworkData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {

    @GET("latest")
    suspend fun getLatest(
        @Query("access_key") accessKey: String
    ): Response<CurrencyNetworkData>

    @GET("symbols")
    suspend fun getSymbols(
        @Query("access_key") accessKey: String
    ): Response<CurrencySymbolsNetworkData>

}