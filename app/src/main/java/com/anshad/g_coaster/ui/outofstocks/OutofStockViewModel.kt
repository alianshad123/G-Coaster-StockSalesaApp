package com.anshad.g_coaster.ui.outofstocks

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anshad.basestructure.model.Event
import com.anshad.basestructure.model.LoadingMessageData
import com.anshad.basestructure.ui.BaseViewModel
import com.anshad.g_coaster.R
import com.anshad.g_coaster.data.repositories.SalesRepository
import com.anshad.g_coaster.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OutofStockViewModel @Inject constructor(
    private val repository: SalesRepository
) : BaseViewModel() {

    var pageLimit: Int =10
    val searchLimit: Int =5
    val _itemsObserveList = MutableLiveData<ItemsModelData?>()
    val itemsObserveListData: MutableLiveData<ItemsModelData?> = _itemsObserveList
    private val loadingLiveData = MutableLiveData<Event<LoadingMessageData>>()
    val loading_: LiveData<Event<LoadingMessageData>> = loadingLiveData

    var itemsArray:ArrayList<ItemsModel> = ArrayList<ItemsModel>()


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

    fun getOutofStocks(){
        showLoading_()
        repository.getOutofStocks(PageLimit(pageLimit=pageLimit)).subscribe({ apiResult ->
            hideLoading_()
            if (apiResult.isSuccess) {

                _itemsObserveList.postValue(apiResult.data)
            } else {
                _itemsObserveList.postValue(null)


            }
        }, {
            hideLoading_()

        })
    }

    fun navigateToAddFragment(itemData: ItemsModel) {
        if(itemData!=null){

            val bundle = Bundle()
            bundle.putBoolean("isEdit", true)
            bundle.putSerializable("item",itemData)
            navigate(R.id.action_outofStockFragment_to_addItemFragment,bundle)

        }

    }

    fun filterData(newText: String?) {
        if((newText?.length ?: 0) <= 0){
            getOutofStocks()
            return
        }
        showLoading_()
        repository.searchOutOfStock(OutOfStockSearch(
            pageLimit=searchLimit,
            search = newText
        )).subscribe({ apiResult ->
            hideLoading_()
            if (apiResult.isSuccess) {

                _itemsObserveList.postValue(apiResult.data)

            } else {
                _itemsObserveList.postValue(null)


            }
        }, {
            hideLoading_()

        })
    }

}