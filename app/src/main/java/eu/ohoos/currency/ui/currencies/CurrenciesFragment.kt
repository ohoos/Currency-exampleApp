package eu.ohoos.currency.ui.currencies

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import eu.ohoos.currency.R
import eu.ohoos.currency.common.DataState
import eu.ohoos.currency.databinding.FragmentCurrenciesBinding
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
class CurrenciesFragment : BaseFragment(R.layout.fragment_currencies) {
    private val binding by viewBinding(FragmentCurrenciesBinding::bind)
    private val viewModel: CurrenciesViewModel by viewModels()
    private lateinit var adapter: CurrencyRateAdapter

    private var dialogShown = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        initList()
        initObservers()
        initListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_currencies, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.favoriteFragment -> {
                findNavController().navigate(CurrenciesFragmentDirections.actionToFavorite())
                true
            }
            else -> super.onContextItemSelected(item)
        }

    private fun initList() {
        adapter = CurrencyRateAdapter()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
    }

    private fun initObservers() {
        viewModel.currencyRates.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                DataState.Loading -> binding.progressBar.isVisible = true
                is DataState.Success -> {
                    if (dataState.data.isEmpty() && !dialogShown) showFavoriteDialog()
                    adapter.submitList(dataState.data)
                    binding.progressBar.isVisible = false
                }
                is DataState.Error -> {
                    binding.mainLayout.snackBarWithAction(
                        message = R.string.common_error_message,
                        actionButtonTitle = R.string.common_error_button
                    ) {
                        viewModel.refresh()
                    }
                    binding.progressBar.isVisible = false
                }
            }
            binding.swipeRefresh.isRefreshing = false
        })
    }

    private fun initListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
            binding.inputAmount.setText("")
        }

        binding.inputAmount.getQueryTextChangeStateFlow()
            .debounce(300)
            .onEach { viewModel.calculateAmount(it) }
            .launchIn(lifecycleScope)
    }

    private fun showFavoriteDialog() {
        dialogShown = true
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(R.string.currencies_favorite_dialog_message)
            .setPositiveButton(R.string.currencies_favorite_dialog_positive_button) { _, _ ->
                dialogShown = false
                findNavController().navigate(CurrenciesFragmentDirections.actionToFavorite())
            }
            .setNegativeButton(R.string.currencies_favorite_dialog_negative_button) { _, _ ->
                // nothing
                dialogShown = false
            }
            .setCancelable(false)
            .create()
        dialog.show()
    }
}