package com.majed.currencyapp.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.majed.currencyapp.data.model.service.Currency
import com.majed.currencyapp.data.remote.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.majed.currencyapp.data.repository.CurrencyRepo

class CurrencyViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: CurrencyRepo = CurrencyRepo()

    private val currencyResponse = MutableLiveData<ApiResponse<Currency>>()

    val currencyResult: LiveData<ApiResponse<Currency>>
        get() = currencyResponse


    fun getCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCurrency().collect { currencyResponse.postValue(it) }
        }
    }

}