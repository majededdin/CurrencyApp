package com.majed.currencyapp.data.application

import android.app.Application
import com.majed.currencyapp.BuildConfig
import com.majed.currencyapp.data.consts.AppConst

class BaseApp : Application() {

    private lateinit var appConst: AppConst

    companion object {
        var instance: BaseApp = BaseApp()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appConst = AppConst.instance
        initAppConst()
    }


    private fun initAppConst() {
        appConst.appInstance = this
        appConst.isDebug = BuildConfig.DEBUG
        appConst.appBaseUrl = BuildConfig.BASE_URL
        appConst.accessKey = "c5ac634e3322c067f88487c48deb47e6"
    }

}