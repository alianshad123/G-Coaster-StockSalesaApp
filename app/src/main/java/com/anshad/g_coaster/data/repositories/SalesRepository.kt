package com.anshad.g_coaster.data.repositories

import com.anshad.basestructure.model.APIResult
import com.anshad.g_coaster.model.*
import io.reactivex.rxjava3.core.Single

interface SalesRepository {
    fun getOutofStocks(): Single<APIResult<ItemsModelData>>
    fun getSalesReport(): Single<APIResult<SalesReportModel>>
    fun getSales(request: SaleFilterDateModel):  Single<APIResult<SoldItemsModel>>

    fun getCurrentSale(request: SaleFilterDateModel):  Single<APIResult<SalesReport>>
    fun getBills(request: SaleFilterDateModel):Single<APIResult<BillsListModel>>
    fun getSalesByBills(request: SalesRequest):  Single<APIResult<SoldItemsModel>>
}