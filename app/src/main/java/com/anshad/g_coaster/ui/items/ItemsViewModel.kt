package com.anshad.g_coaster.ui.items

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.anshad.basestructure.model.Event
import com.anshad.basestructure.model.LoadingMessageData
import com.anshad.basestructure.ui.BaseViewModel
import com.anshad.g_coaster.App
import com.anshad.g_coaster.R
import com.anshad.g_coaster.data.repositories.ItemsRepository
import com.anshad.g_coaster.data.repositories.PreferenceProvider
import com.anshad.g_coaster.model.AddItemModel
import com.anshad.g_coaster.model.ItemsModel
import com.anshad.g_coaster.model.ItemsModelData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val preferenceProvider: PreferenceProvider,
    private val repository: ItemsRepository
) : BaseViewModel() {



    val _itemsObserveList = MutableLiveData<ItemsModelData?>()
    val itemsObserveListData: MutableLiveData<ItemsModelData?> = _itemsObserveList

    var itemsArray:ArrayList<ItemsModel> = ArrayList<ItemsModel>()

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

    fun getItems(){
        showLoading_()
        repository.getItems().subscribe({ apiResult ->
            hideLoading_()
            if (apiResult.isSuccess) {

                _itemsObserveList.postValue(apiResult.data)
                apiResult.data?.result?.forEach {
                    itemsArray.add(it)
                }
            } else {
                _itemsObserveList.postValue(null)

            }
        }, {
            hideLoading_()

        })
    }

    fun navigateToAddFragment(item: ItemsModel?) {
        if(item!=null){

            val bundle = Bundle()
            bundle.putBoolean("isEdit", true)
            bundle.putSerializable("item",item)
            navigate(R.id.action_itemsFragment_to_addItemFragment,bundle)

        }else{
            val bundle = Bundle()
            bundle.putBoolean("isEdit", false)
            navigate(R.id.action_itemsFragment_to_addItemFragment,bundle)
        }

    }

    fun deleteItem(item: ItemsModel) {
            showLoading_()
            repository.deleteItem(
                AddItemModel(
                    id=item.id
                )
            ).subscribe({ result ->
                hideLoading_()
                if (result.isSuccess) {
                    Toast.makeText(App.instance.applicationContext,"Item Deleted",Toast.LENGTH_SHORT).show()

                   getItems()


                } else {


                }
            }, {
                hideLoading_()

            })
        }




}