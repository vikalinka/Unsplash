package lt.vitalikas.unsplash.ui.feed_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.ItemFeedBinding
import lt.vitalikas.unsplash.domain.models.FeedPhoto

class FeedAdapter(private val onItemClick: (id: String) -> Unit) :
    PagingDataAdapter<FeedPhoto, FeedAdapter.FeedPhotoViewHolder>(FeedPhotoDiffUtilCallback()) {

    class FeedPhotoViewHolder(
        private val binding: ItemFeedBinding,
        onItemClick: (id: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var id: String

        init {
            binding.root.setOnClickListener {
                onItemClick(id)
            }
        }

        fun bind(item: FeedPhoto) {
            id = item.id

            Glide.with(itemView)
                .load(item.urls.regular)
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(binding.ivPhoto)

            Glide.with(itemView)
                .load(item.user.imageUser.medium)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedPhotoViewHolder =
        FeedPhotoViewHolder(
            binding = ItemFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onItemClick
        )

    override fun onBindViewHolder(holder: FeedPhotoViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }


//    override fun onViewAttachedToWindow(holder: FeedPhotoViewHolder) {
//        super.onViewAttachedToWindow(holder)
//        if (holder.layoutPosition == 0) {
//            val p =
//                holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
//            p.isFullSpan = true
//        }
//    }

    class FeedPhotoDiffUtilCallback : DiffUtil.ItemCallback<FeedPhoto>() {
        override fun areItemsTheSame(oldItem: FeedPhoto, newItem: FeedPhoto): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FeedPhoto, newItem: FeedPhoto): Boolean =
            oldItem == newItem
    }
}