package com.majed.currencyapp.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface ApiService {

    @GET("api/latest")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getCurrencyRates(
        @QueryMap map: HashMap<String, Any>
    ): ResponseBody

}