package com.majed.currencyapp.data.repository

import com.google.gson.reflect.TypeToken
import com.majed.currencyapp.data.consts.AppConst
import com.majed.currencyapp.data.consts.Params.Companion.ACCESS_KEY
import com.majed.currencyapp.data.model.service.Currency
import com.majed.currencyapp.data.remote.ApiClient
import com.majed.currencyapp.data.remote.ApiResponse
import com.majed.currencyapp.data.remote.ApiStatus
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

open class CurrencyRepo {

    private var apiService = ApiClient.getInstance()

    //---------------------------------------- ApiResponse Methods ---------------------------------

    private fun <M> getApiError(throwable: Throwable) = ApiResponse<M>().getErrorBody(throwable)

    //---------------------------------------- Global Methods ---------------------------------

    fun getCurrency() = flow {
        emit(ApiResponse(ApiStatus.OnLoading))

        val map: HashMap<String, Any> = java.util.HashMap()

        map[ACCESS_KEY] = AppConst.instance.accessKey

        val currencyResponse =
            ApiResponse<Currency>(
                apiService.getCurrencyRates(map).string(), object : TypeToken<Currency>() {}.type
            )

        emit(currencyResponse.getApiResult())
    }.catch { emit(getApiError(it)) }

}