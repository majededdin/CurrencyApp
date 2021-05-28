package com.majed.currencyapp.ui.view.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.majed.currencyapp.anagrams.AnagramsChecker
import com.majed.currencyapp.data.consts.Params.Companion.RATE
import com.majed.currencyapp.data.model.modified.ErrorHandler
import com.majed.currencyapp.data.model.service.Currency
import com.majed.currencyapp.data.model.service.RatesWithName
import com.majed.currencyapp.data.remote.ApiResponse
import com.majed.currencyapp.data.remote.ApiStatus
import com.majed.currencyapp.databinding.ActivityMainBinding
import com.majed.currencyapp.ui.base.BaseActivity
import com.majed.currencyapp.ui.view.adapters.RatesAdapter
import com.majed.currencyapp.ui.viewModel.CurrencyViewModel
import com.majed.currencyapp.utils.extentionUtils.intentNormal
import com.majed.currencyapp.utils.extentionUtils.toGone
import com.majed.currencyapp.utils.extentionUtils.toVisible
import com.majed.currencyapp.utils.recyclerUtils.SimpleDividerItemDecoration
import org.json.JSONObject

class MainActivity : BaseActivity<CurrencyViewModel>(), RatesAdapter.RatesCallBack {

    private lateinit var currencyVM: CurrencyViewModel

    private lateinit var binding: ActivityMainBinding

    private var apiResponse: ApiResponse<Currency> = ApiResponse()

    private lateinit var adapter: RatesAdapter


    override fun getViewModel(): Class<CurrencyViewModel> = CurrencyViewModel::class.java


    override fun viewModelInstance(viewModel: CurrencyViewModel?) {
        currencyVM = viewModel!!
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewInit()
        updateView()

        println("AnagramsChecker : " + AnagramsChecker("debit card", "bad credit").check())
        println("AnagramsChecker : " + AnagramsChecker("punishments", "nine thumps").check())

        currencyVM.currencyResult.observe(this, this::currencyResult)
    }


    private fun currencyResult(apiResponse: ApiResponse<Currency>) {
        this.apiResponse = apiResponse
        handleApiResponse(apiResponse) { updateView() }
        if (apiResponse.apiStatus == ApiStatus.OnSuccess) {

            binding.txtBase.text = apiResponse.model!!.base
            getListOfRates(apiResponse.modelAsJSONObject)
        }
    }

    private fun getListOfRates(jsonObject: JSONObject) {
        val rates: JSONObject = jsonObject.getJSONObject("rates")

        val x: Iterator<String> = rates.keys()

        val list: ArrayList<RatesWithName> = ArrayList()

        while (x.hasNext()) {
            list.add(RatesWithName(x.next(), rates.getDouble(x.next())))
        }

        setResponseResult(list)
    }


    private fun setResponseResult(list: List<RatesWithName>) {
        if (list.isNotEmpty()) {
            binding.recyclerRates.toVisible()
            adapter.setItem(list)
        } else {
            binding.recyclerRates.toGone()
            adapter.clear()
        }
    }


    override fun updateView() {
        currencyVM.getCurrency()
    }


    override fun setErrorHandler(handler: ErrorHandler) {
    }


    override fun viewInit() {
        adapter = RatesAdapter(this)
        binding.recyclerRates.adapter = adapter
        binding.recyclerRates.layoutManager = LinearLayoutManager(this)
        binding.recyclerRates.addItemDecoration(SimpleDividerItemDecoration(this))
    }


    override fun onDefaultClicked(ratesWithName: RatesWithName) {
        val bundle = Bundle()
        bundle.putParcelable(RATE, ratesWithName)
        intentNormal(ExchangeCurrencyActivity::class.java, bundle)
    }

}