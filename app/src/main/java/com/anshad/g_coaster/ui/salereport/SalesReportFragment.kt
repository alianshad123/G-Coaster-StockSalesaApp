package com.anshad.g_coaster.ui.salereport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.anshad.basestructure.model.LoadingMessageData
import com.anshad.basestructure.ui.BaseFragment
import com.anshad.basestructure.utils.EventObserver
import com.anshad.g_coaster.R
import com.anshad.g_coaster.databinding.FragmentSalesReportBinding
import com.anshad.g_coaster.model.SalesReport

import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SalesReportFragment :  BaseFragment<SalesReportViewModel>(R.layout.fragment_sales_report),
    ItemClickListner {


    private var _binding: FragmentSalesReportBinding? = null
    private val binding get() = _binding!!

    override val viewModel: SalesReportViewModel by viewModels()
    private val adapter = SalesReportAdapter(listOf(), this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSalesReportBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        viewModel.getSalesReport()
        viewModel.getCurrentSale()

        viewModel.salesReportData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.result?.size!! > 0) {
                    adapter.updateData(it?.result?.asReversed()!!)
                }

            }

        }
        viewModel.loading_.observe(viewLifecycleOwner, EventObserver {
            _onLoadingMessage(it)
        })




        binding.currentView.setOnClickListener {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val date=format.format(Date())
            viewModel.showCurrentReport(date)

        }
    }
    private fun _onLoadingMessage(messageData: LoadingMessageData) {
        if (messageData.context == null) {
            messageData.context = requireContext()
        }
        binding.isLoading = messageData.isLoading
        binding.message = messageData.getMessage()

    }




    override fun onDataItemClicked(salesReport: SalesReport) {
        viewModel.showDetailedReport(salesReport)
    }

}