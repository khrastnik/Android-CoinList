package com.coinlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.coinlist.R
import com.coinlist.common.Resource
import com.coinlist.common.gone
import com.coinlist.common.visible
import com.coinlist.databinding.CoinListFragmentBinding
import com.coinlist.ui.base.BaseFragment
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CoinListFragment : BaseFragment() {

    private lateinit var adapter: CoinListAdapter
    private var counterAdapter: ArrayAdapter<String>? = null
    private var fragmentBinding: CoinListFragmentBinding? = null
    private val binding get() = fragmentBinding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(CoinListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = CoinListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupCounterSpinner()
        setupPairPriceObserver()
    }

    private fun setupListAdapter() {
        binding.rvCoinList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCoinList.itemAnimator = null
        adapter = CoinListAdapter(arrayListOf())
        binding.rvCoinList.addItemDecoration(
            DividerItemDecoration(
                binding.rvCoinList.context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rvCoinList.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            getPairsWithPrices()
        }
    }

    private fun setupCounterSpinner() {
        if (counterAdapter == null) {
            counterAdapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item, viewModel.getCounterItems()
            )
            binding.spinnerCounter.adapter = counterAdapter
            binding.spinnerCounter.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedCounter = counterAdapter!!.getItem(position)
                    renderListBySelectedCounter(selectedCounter)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        } else {
            renderCounterSpinner()
        }
    }

    private fun renderCounterSpinner() {
        if (counterAdapter != null && counterAdapter!!.isEmpty) {
            counterAdapter!!.clear()
            counterAdapter!!.addAll(viewModel.getCounterItems())
        }
    }

    private fun renderListBySelectedCounter(selectedItem: String?) {
        if (selectedItem != null) {
            viewModel.setSelectedCounterName(selectedItem)
            renderCoinList()
        }
    }

    private fun setupPairPriceObserver() {
        viewModel.getPairPriceList().onEach {
            when (it) {
                is Resource.Loading -> {
                    setRecyclerViewVisible(false)
                    setProgressBarVisible(true)
                }
                is Resource.Success -> {
                    setProgressBarVisible(false)
                    renderCounterSpinner()
                    renderCoinList()
                    setRecyclerViewVisible(true)
                }
                is Resource.Error -> {
                    showErrorMessage(getString(R.string.api_error_message))
                }
                is Resource.NoInternet -> {
                    showErrorMessage(getString(R.string.no_internet_error_message))
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setProgressBarVisible(visible: Boolean) {
        if (visible) {
            binding.progressBar.visible()
        } else {
            binding.progressBar.gone()
        }
    }

    private fun setRecyclerViewVisible(visible: Boolean) {
        if (visible) {
            binding.rvCoinList.visible()
        } else {
            binding.rvCoinList.gone()
        }
    }

    private fun renderCoinList() {
        viewLifecycleOwner.lifecycleScope.launch(Main) {
            val listItems = withContext(Default) {
                viewModel.getListItems()
            }
            adapter.setItems(listItems)
        }
    }

    private fun showErrorMessage(message: String) {
        setProgressBarVisible(false)
        showSnackBar(message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTickerJob()
    }

    override fun onResume() {
        super.onResume()
        getPairsWithPrices()
    }

    private fun getPairsWithPrices() {
        viewModel.getPairWithPrice()
    }

    companion object {
        fun newInstance() = CoinListFragment()
    }
}
