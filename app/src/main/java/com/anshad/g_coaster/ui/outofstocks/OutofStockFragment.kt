package com.anshad.g_coaster.ui.outofstocks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.anshad.basestructure.model.LoadingMessageData
import com.anshad.basestructure.ui.BaseFragment
import com.anshad.basestructure.utils.EventObserver
import com.anshad.g_coaster.R
import com.anshad.g_coaster.databinding.FragmentOutofStockBinding
import com.anshad.g_coaster.databinding.FragmentSaleItemsBinding
import com.anshad.g_coaster.databinding.FragmentSalesReportBinding
import com.anshad.g_coaster.model.ItemsModel
import com.anshad.g_coaster.ui.saleitems.SaleItemsAdapter
import com.anshad.g_coaster.ui.saleitems.SaleItemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OutofStockFragment : BaseFragment<OutofStockViewModel>(R.layout.fragment_outof_stock),
    ItemClickListner {
    private var _binding: FragmentOutofStockBinding? = null
    private val binding get() = _binding!!

    override val viewModel: OutofStockViewModel by viewModels()
    private val adapter = OutofStockAdapter(listOf(), this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOutofStockBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getOutofStocks()

        viewModel.itemsObserveListData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.result?.size!! > 0) {
                    adapter.updateData(it?.result!!)
                }

            }

        }
        viewModel.loading_.observe(viewLifecycleOwner, EventObserver {
            _onLoadingMessage(it)
        })

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {


                // adapter.updateData( adapter.data.filter { it.name?.contains(query.toString()) == true ||it.name?.contains(query.toString()) == true })

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // if query text is change in that case we
                // are filtering our adapter with
                // new text on below line.
                //  adapter.updateData( adapter.data.filter { it.name?.contains(newText.toString()) == true ||it.name?.contains(newText.toString()) == true })

                // filterData(newText);
                viewModel.filterData(newText);
                return false
            }
        })

        binding.recyclerview?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val height=binding.recyclerview.height
                val diff = height-dy
                if(diff < 1000){
                     viewModel.pageLimit=viewModel.pageLimit+10
                     viewModel.getOutofStocks()
                }
            }
        })
    }

    private fun _onLoadingMessage(messageData: LoadingMessageData) {
        if (messageData.context == null) {
            messageData.context = requireContext()
        }
        binding.isLoading = messageData.isLoading
        binding.message = messageData.getMessage()

    }



    override fun onDataItemClicked(itemData: ItemsModel) {
        viewModel.navigateToAddFragment(itemData)
    }


}