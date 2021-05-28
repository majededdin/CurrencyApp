package com.majed.currencyapp.utils.extentionUtils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

// ------------------------------------------- Activity Normal -------------------------------------

fun Activity.intentNormal(aClass: Class<*>, bundle: Bundle) = intentNormal(aClass, bundle, false)

fun Activity.intentNormal(aClass: Class<*>, bundle: Bundle, finish: Boolean) {
    startActivity(Intent(this, aClass).putExtras(bundle))
    if (finish) finish()
}
