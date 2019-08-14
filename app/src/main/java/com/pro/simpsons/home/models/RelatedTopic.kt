package com.pro.simpsons.home.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RelatedTopic(
    val FirstURL: String,
    val Icon: Icon,
    val Result: String,
    val Text: String
) : Parcelable