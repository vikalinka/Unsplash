package lt.vitalikas.unsplash.ui.favorites_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import lt.vitalikas.unsplash.databinding.LoadStateBinding

class SearchLoadStateAdapter(
    private val onRetryButtonClick: () -> Unit
) : LoadStateAdapter<SearchLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(
        private val binding: LoadStateBinding,
        onRetryButtonClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.bRetry.setOnClickListener {
                onRetryButtonClick()
            }
        }

        fun bind(loadState: LoadState) = with(binding) {
            pbLoading.isVisible = loadState is LoadState.Loading
            tvLoadStateErrorMsg.isVisible = loadState is LoadState.Error
            bRetry.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder =
        LoadStateViewHolder(
            binding = LoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onRetryButtonClick = onRetryButtonClick
        )
}