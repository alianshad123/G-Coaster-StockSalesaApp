package com.anshad.g_coaster.data.datasource.sales

import com.anshad.basestructure.datasources.BaseRemote
import com.anshad.basestructure.ktx.Observable.applyNetworkSchedulers
import com.anshad.basestructure.model.APIResult
import com.anshad.g_coaster.api.ApiService
import com.anshad.g_coaster.model.*
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SalesRemoteDataSource @Inject constructor(private val apiService: ApiService) :
    BaseRemote(), SalesDataSource.Remote {
    override fun getOutofStocks(): Single<APIResult<ItemsModelData>> {
        return apiService.getOutofStocks().createResult().applyNetworkSchedulers()
    }

    override fun getSalesReport(): Single<APIResult<SalesReportModel>> {
        return apiService.getSalesReport().createResult().applyNetworkSchedulers()
    }

    override fun getSales(request: SaleFilterDateModel): Single<APIResult<SoldItemsModel>> {
        return apiService.getSales(request).createResult().applyNetworkSchedulers()
    }

    override fun getCurrentSale(request: SaleFilterDateModel): Single<APIResult<SalesReport>> {
        return apiService.getCurrentSale(request).createResult().applyNetworkSchedulers()
    }

    override fun getBills(request: SaleFilterDateModel): Single<APIResult<BillsListModel>> {
        return apiService.getBills(request).createResult().applyNetworkSchedulers()
    }

    override fun getSalesByBills(request: SalesRequest): Single<APIResult<SoldItemsModel>> {
        return apiService.getSalesByBills(request).createResult().applyNetworkSchedulers()
    }


}