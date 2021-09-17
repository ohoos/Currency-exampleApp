package eu.ohoos.currency.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import eu.ohoos.currency.R
import eu.ohoos.currency.common.DataState
import eu.ohoos.currency.databinding.FragmentFavoriteBinding
import eu.ohoos.currency.ui.base.BaseFragment
import eu.ohoos.currency.ui.base.viewBinding
import eu.ohoos.currency.ui.extensions.getQueryTextChangeStateFlow
import eu.ohoos.currency.ui.extensions.snackBarWithAction
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@AndroidEntryPoint
class FavoriteFragment : BaseFragment(R.layout.fragment_favorite) {
    private val binding by viewBinding(FragmentFavoriteBinding::bind)
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        initData()
        initListeners()
    }

    private fun initList() {
        adapter = FavoriteAdapter {
            viewModel.addOrRemoveFavoriteCurrency(it)
        }

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
    }

    private fun initData() {
        viewModel.sortedCurrencyRates.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                DataState.Loading -> binding.progressBarLoading.isVisible = true
                is DataState.Success -> {
                    adapter.submitList(dataState.data)
                    binding.progressBarLoading.isVisible = false
                }
                is DataState.Error -> {
                    binding.mainLayout.snackBarWithAction(
                        message = R.string.common_error_message,
                        actionButtonTitle = R.string.common_error_button
                    ) {
                        viewModel.refresh()
                    }
                    binding.progressBarLoading.isVisible = false
                }
            }
            binding.swipeRefresh.isRefreshing = false
        })
    }

    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        binding.inputSearch.getQueryTextChangeStateFlow()
            .debounce(300)
            .onEach { viewModel.filterCurrencies(it) }
            .launchIn(lifecycleScope)
    }
}