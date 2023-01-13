package com.anshad.g_coaster.ui.salereport

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anshad.basestructure.model.Event
import com.anshad.basestructure.model.LoadingMessageData
import com.anshad.basestructure.ui.BaseViewModel
import com.anshad.g_coaster.R
import com.anshad.g_coaster.data.repositories.SalesRepository
import com.anshad.g_coaster.model.SaleFilterDateModel
import com.anshad.g_coaster.model.SalesReport
import com.anshad.g_coaster.model.SalesReportModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SalesReportViewModel @Inject constructor(
    private val repository: SalesRepository
) : BaseViewModel() {

    val _salesReportData = MutableLiveData<SalesReportModel?>()
    val salesReportData: MutableLiveData<SalesReportModel?> = _salesReportData

    val _currentSale=MutableLiveData<SalesReport?>()
    val currentSale: MutableLiveData<SalesReport?> = _currentSale

    private val loadingLiveData = MutableLiveData<Event<LoadingMessageData>>()
    val loading_: LiveData<Event<LoadingMessageData>> = loadingLiveData

    fun showLoading_(message: LoadingMessageData) {
        loadingLiveData.value = Event(message)
    }

    fun showLoading_(@StringRes message: Int = R.string.loading) {
        val messageData = LoadingMessageData()
        messageData.isLoading = true
        messageData.messageRes = message
        showLoading_(messageData)
    }

    fun hideLoading_() {
        val messageData = LoadingMessageData()
        messageData.isLoading = false
        showLoading_(messageData)
    }

    fun getSalesReport() {
        showLoading_()
        repository.getSalesReport().subscribe({ apiResult ->
            hideLoading_()
            if (apiResult.isSuccess) {

                _salesReportData.postValue(apiResult.data)
            } else {
                _salesReportData.postValue(null)


            }
        }, {
            hideLoading_()

        })
    }

    fun showDetailedReport(salesReport: SalesReport) {
        val bundle=Bundle()
        bundle.putString("dateKey",salesReport.saledate)
        navigate(R.id.action_salesreportFragment_to_billsFragment,bundle)

    }

    fun getCurrentSale() {

        val format = SimpleDateFormat("yyyy-MM-dd")
       val date=format.format(Date())
        showLoading()
        repository.getCurrentSale(SaleFilterDateModel(updatedAt = date)).subscribe({ apiResult ->
            hideLoading()
            if (apiResult.isSuccess) {
                if(apiResult.data!=null ){
                    _currentSale.postValue(apiResult.data)
                }


            } else {



            }
        }, {
            hideLoading()

        })
    }

    fun showCurrentReport(date: String) {
        val bundle=Bundle()
        bundle.putString("dateKey",date)
        navigate(R.id.action_salesreportFragment_to_billsFragment,bundle)
    }


}