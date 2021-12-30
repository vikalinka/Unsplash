package lt.vitalikas.unsplash.ui.feed_screen

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import lt.vitalikas.unsplash.domain.models.FeedPhoto

class FeedAdapter : AsyncListDifferDelegationAdapter<FeedPhoto>(FeedPhotoDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(FeedAdapterDelegate())
    }

    class FeedPhotoDiffUtilCallback : DiffUtil.ItemCallback<FeedPhoto>() {
        override fun areItemsTheSame(oldItem: FeedPhoto, newItem: FeedPhoto): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FeedPhoto, newItem: FeedPhoto): Boolean =
            oldItem == newItem
    }
}