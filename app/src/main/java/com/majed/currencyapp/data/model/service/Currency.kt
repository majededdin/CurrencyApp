package com.majed.currencyapp.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    var success: Boolean,
    var timestamp: Long,
    var base: String,
    var date: String
//    var rates: Rate
) : Parcelable