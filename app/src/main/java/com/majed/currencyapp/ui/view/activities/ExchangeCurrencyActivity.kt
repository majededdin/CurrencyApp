package com.majed.currencyapp.ui.view.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import com.majed.currencyapp.data.consts.Params.Companion.EUR
import com.majed.currencyapp.data.consts.Params.Companion.RATE
import com.majed.currencyapp.data.model.modified.ErrorHandler
import com.majed.currencyapp.data.model.service.RatesWithName
import com.majed.currencyapp.databinding.ActivityExchangeCurrencyBinding
import com.majed.currencyapp.ui.base.BaseActivity
import com.majed.currencyapp.ui.viewModel.CurrencyViewModel

class ExchangeCurrencyActivity : BaseActivity<CurrencyViewModel>(), TextWatcher {

    private lateinit var currencyVM: CurrencyViewModel

    private lateinit var binding: ActivityExchangeCurrencyBinding

    private lateinit var ratesWithName: RatesWithName


    override fun getViewModel(): Class<CurrencyViewModel> = CurrencyViewModel::class.java


    override fun viewModelInstance(viewModel: CurrencyViewModel?) {
        currencyVM = viewModel!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangeCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewInit()
        updateView()

    }


    override fun updateView() {
        binding.editBaseValue.setText("1.0")

        binding.txtBaseName.text = EUR
        binding.txtRateName.text = ratesWithName.name
        binding.txtRateValue.text = ratesWithName.value.toString()

    }


    override fun setErrorHandler(handler: ErrorHandler) {
    }


    override fun viewInit() {
        ratesWithName = intent.getParcelableExtra(RATE)!!

        binding.editBaseValue.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            };

        binding.editBaseValue.addTextChangedListener(this)
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        calculateRate(s)
    }

    override fun afterTextChanged(s: Editable?) {
    }

    private fun calculateRate(s: CharSequence?) {
        if (!s.isNullOrEmpty())
            binding.txtRateValue.text = (s.toString().toDouble() * ratesWithName.value).toString()
        else
            binding.txtRateValue.text = ratesWithName.value.toString()
    }

}