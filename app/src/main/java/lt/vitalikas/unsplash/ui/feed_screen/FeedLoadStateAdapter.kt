package lt.vitalikas.unsplash.ui.feed_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import lt.vitalikas.unsplash.databinding.LoadStateFeedBinding

class FeedLoadStateAdapter : LoadStateAdapter<FeedLoadStateAdapter.LoadStateViewHolder>() {

    class LoadStateViewHolder(
        private val binding: LoadStateFeedBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.Loading -> binding.pbLoading.isVisible = true
                else -> binding.pbLoading.isVisible = false
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder =
        LoadStateViewHolder(
            LoadStateFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}