package lt.vitalikas.unsplash.ui.collection_with_photos_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.ItemFeedBinding
import lt.vitalikas.unsplash.domain.models.collections.CollectionPhoto

class CollectionAdapter(
    private val onItemClick: (id: String) -> Unit,
    private val onLikeClick: (id: String) -> Unit,
    private val onDislikeClick: (id: String) -> Unit
) : PagingDataAdapter<CollectionPhoto, CollectionAdapter.CollectionPhotoViewHolder>(
    CollectionPhotoComparator()
) {

    inner class CollectionPhotoViewHolder(
        private val binding: ItemFeedBinding,
        onItemClick: (id: String) -> Unit,
        private val onLikeClick: (id: String) -> Unit,
        private val onDislikeClick: (id: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var id: String

        init {
            binding.root.setOnClickListener {
                onItemClick(id)
            }
        }

        fun bind(item: CollectionPhoto) {
            id = item.id

            Glide.with(itemView)
                .load(item.urls.regular)
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(binding.ivPhoto)

            Glide.with(itemView)
                .load(item.user.profileImage.medium)
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(binding.ivAvatar)

            binding.tvName.text = item.user.name
            binding.tvUsername.text =
                itemView.resources.getString(R.string.username, item.user.username)

            with(binding.ivLove) {
                if (item.likedByUser) {
                    setImageResource(R.drawable.ic_love_filled)
                    setColorFilter(ContextCompat.getColor(context, R.color.red))
                    setOnClickListener {
                        onDislikeClick(item.id)
                    }
                } else {
                    setImageResource(R.drawable.ic_love)
                    setColorFilter(ContextCompat.getColor(context, R.color.red))
                    setOnClickListener {
                        onLikeClick(item.id)
                    }
                }

                binding.tvLove.text = item.likes.toString()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionPhotoViewHolder =
        CollectionPhotoViewHolder(
            binding = ItemFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onItemClick,
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick
        )

    override fun onBindViewHolder(holder: CollectionPhotoViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    class CollectionPhotoComparator : DiffUtil.ItemCallback<CollectionPhoto>() {
        override fun areItemsTheSame(
            oldItem: CollectionPhoto,
            newItem: CollectionPhoto
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CollectionPhoto,
            newItem: CollectionPhoto
        ): Boolean = oldItem == newItem
    }
}