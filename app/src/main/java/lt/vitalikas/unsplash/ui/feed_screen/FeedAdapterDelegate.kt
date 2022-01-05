package lt.vitalikas.unsplash.ui.feed_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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

            Glide.with(itemView)
                .load(item.user.image.medium)
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(binding.ivAvatar)

            binding.tvName.text = item.user.name
            binding.tvUsername.text =
                itemView.resources.getString(R.string.username, item.user.username)

            binding.ivLove.run {
                setImageResource(R.drawable.ic_love_filled)
                setColorFilter(ContextCompat.getColor(context, R.color.red))
            }.takeIf { item.likedByUser } ?: binding.ivLove.run {
                setImageResource(R.drawable.ic_love)
                setColorFilter(ContextCompat.getColor(context, R.color.red))
            }

            binding.tvLove.text = item.likes.toString()
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

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.layoutPosition == 0) {
            val p =
                holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            p.isFullSpan = true
        }
    }
}