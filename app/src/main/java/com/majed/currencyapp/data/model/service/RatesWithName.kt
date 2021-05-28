package com.majed.currencyapp.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RatesWithName(
    var name: String,
    var value: Double
) : Parcelable