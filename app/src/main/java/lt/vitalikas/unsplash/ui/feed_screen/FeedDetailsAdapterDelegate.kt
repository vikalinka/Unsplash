package lt.vitalikas.unsplash.ui.feed_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import lt.vitalikas.unsplash.databinding.ItemFeedBinding
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails

class FeedDetailsAdapterDelegate :
    AbsListItemAdapterDelegate<FeedPhotoDetails, FeedPhotoDetails, FeedDetailsAdapterDelegate.FeedPhotoDetailsViewHolder>() {

    class FeedPhotoDetailsViewHolder(
        private val binding: ItemFeedBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FeedPhotoDetails) {

        }
    }

    override fun isForViewType(
        item: FeedPhotoDetails,
        items: MutableList<FeedPhotoDetails>,
        position: Int
    ): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): FeedPhotoDetailsViewHolder =
        FeedPhotoDetailsViewHolder(
            ItemFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        item: FeedPhotoDetails,
        holder: FeedPhotoDetailsViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)
}