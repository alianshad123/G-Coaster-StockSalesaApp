package com.anshad.g_coaster.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OutOfStockSearch (
    @SerializedName("pageLimit")
    @Expose
    var pageLimit: Int? = 0,
    @SerializedName("search")
    @Expose
    var search: String? = null)