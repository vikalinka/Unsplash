package lt.vitalikas.unsplash.ui.feed_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.ItemFeedBinding
import lt.vitalikas.unsplash.domain.models.FeedPhoto

class FeedAdapterDelegate :
    AbsListItemAdapterDelegate<FeedPhoto, FeedPhoto, FeedAdapterDelegate.FeedPhotoViewHolder>() {

    class FeedPhotoViewHolder(
        private val binding: ItemFeedBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FeedPhoto) {
            Glide.with(itemView)
                .load(item.urls.regular)
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(binding.ivPhoto)
        }
    }

    override fun isForViewType(
        item: FeedPhoto,
        items: MutableList<FeedPhoto>,
        position: Int
    ): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): FeedPhotoViewHolder =
        FeedPhotoViewHolder(
            ItemFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        item: FeedPhoto,
        holder: FeedPhotoViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)
}