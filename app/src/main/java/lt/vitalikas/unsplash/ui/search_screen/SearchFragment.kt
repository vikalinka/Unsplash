package lt.vitalikas.unsplash.ui.search_screen

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatus
import lt.vitalikas.unsplash.databinding.FragmentSearchBinding
import lt.vitalikas.unsplash.utils.autoCleaned
import lt.vitalikas.unsplash.utils.onTextChangedFlow
import lt.vitalikas.unsplash.utils.showInfo

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val searchList get() = binding.searchRecyclerView
    private val noConnectionText get() = binding.noConnectionTextView
    private val loadingProgress get() = binding.loadingProgressBar
    private val refreshLayout get() = binding.searchSwipeRefreshLayout
    private val toolbar get() = binding.searchToolbar
    private val search get() = binding.queryTextInput

    private val searchViewModel by viewModels<SearchViewModel>()

    private val searchAdapter by autoCleaned {
        SearchAdapter(
            onItemClick = { id ->
                val directions =
                    SearchFragmentDirections.actionSearchFragmentToFeedDetailsFragment(id)
                findNavController().navigate(directions)
            },
            onLikeClick = { id ->
                //
            },
            onDislikeClick = { id ->
                //
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchList()
        initAdapterRefresh()
        observeNetworkConnection()
        observeSearchResults()
        setupToolbar()
        handleToolbarNavigation()
    }

    private fun initSearchList() {
        with(searchList) {
            val searchLoadStateAdapter = SearchLoadStateAdapter {
                searchAdapter.retry()
            }

            val concatAdapter = searchAdapter.withLoadStateFooter(
                footer = searchLoadStateAdapter
            )

            adapter = concatAdapter

            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

            setHasFixedSize(true)

            addItemDecoration(SearchOffsetDecoration(requireContext()))
        }
    }

    private fun observeSearchResults() {
        val query = search.onTextChangedFlow()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchViewModel.getSearchData(query)
                        .collectLatest { data ->
                            searchAdapter.submitData(data)
                            refreshLayout.isRefreshing = false
                        }
                }
            }
        }
    }

    private fun observeNetworkConnection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchViewModel.networkStatus.collect { status ->
                        when (status) {
                            NetworkStatus.Available -> {
                                noConnectionText.isVisible = false
                                // retry after connection re-established
                                searchAdapter.retry()
                            }
                            NetworkStatus.Unavailable -> {
                                noConnectionText.isVisible = true
                                showInfo("No internet connection. Cached data is shown.")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initAdapterRefresh() {
        refreshLayout.setOnRefreshListener {
            searchAdapter.refresh()
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            title = "SEARCH"

            inflateMenu(R.menu.feed_toolbar_menu)

//            setOnMenuItemClickListener { menuItem ->
//                when (menuItem.itemId) {
//                    R.id.toolbar_menu_search -> {
//
//                        menuItem.setOnActionExpandListener(object :
//                            MenuItem.OnActionExpandListener {
//                            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
//                                showToast("search expanded")
//                                return true
//                            }
//
//                            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
//                                showToast("search collapsed")
//                                return true
//                            }
//                        })
//
//                        with(menuItem.actionView as SearchView) {
//                            this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                                override fun onQueryTextSubmit(p0: String?): Boolean {
//                                    return true
//                                }
//
//                                override fun onQueryTextChange(text: String?): Boolean {
//                                    return true
//                                }
//                            })
//                        }
//
//                        true
//                    }
//
//                    else -> false
//                }
//            }
        }
    }

    private fun handleToolbarNavigation() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}