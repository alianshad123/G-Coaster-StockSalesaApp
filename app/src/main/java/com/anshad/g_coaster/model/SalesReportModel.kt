package com.anshad.g_coaster.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SalesReportModel {
    @SerializedName("data")
    @Expose
    var result: ArrayList<SalesReport>? = null
}