package com.majed.currencyapp.data.consts

import android.app.Application

class AppConst {

    var isDebug = false
    lateinit var appInstance: Application
    lateinit var appBaseUrl: String
    lateinit var accessKey: String

    companion object {
        var instance = AppConst()
    }

}