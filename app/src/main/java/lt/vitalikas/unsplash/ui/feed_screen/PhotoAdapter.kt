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
import lt.vitalikas.unsplash.domain.models.photo.Photo

class PhotoAdapter(
    private val onItemClick: (id: String) -> Unit,
    private val onDownloadClick: (id: String, photoDownloadUrl: String) -> Unit,
    private val onLikeClick: (id: String) -> Unit,
    private val onDislikeClick: (id: String) -> Unit
) : PagingDataAdapter<Photo, PhotoAdapter.PhotoViewHolder>(PhotoComparator()) {

    inner class PhotoViewHolder(
        private val binding: ItemFeedBinding,
        private val onDownloadClick: (id: String, photoDownloadUrl: String) -> Unit,
        private val onLikeClick: (id: String) -> Unit,
        private val onDislikeClick: (id: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Photo) {

            Glide.with(itemView)
                .load(item.url.regular)
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(binding.photoImageView)

            Glide.with(itemView)
                .load(item.user.profileImage.medium)
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(binding.avatarShapeableImageView)

            binding.root.setOnClickListener {
                onItemClick(item.id)
            }

            binding.nameTextView.text = item.user.name
            binding.usernameTextView.text =
                itemView.resources.getString(R.string.username, item.user.username)

            binding.downloadImageView.setOnClickListener {
                val id = item.id
                val uri = item.link.downloadLocation
                onDownloadClick(id, uri)
            }

            with(binding.likeImageView) {
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
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder(
            binding = ItemFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onDownloadClick = onDownloadClick,
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick
        )

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    class PhotoComparator : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem == newItem
    }
}