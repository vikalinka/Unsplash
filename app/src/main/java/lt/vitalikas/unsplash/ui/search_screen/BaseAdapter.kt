package lt.vitalikas.unsplash.ui.search_screen

import android.annotation.SuppressLint
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
import lt.vitalikas.unsplash.domain.models.search.SearchPhoto

class BaseAdapter<T : Any>(
    private val onItemClick: (id: String) -> Unit,
    private val onLikeClick: (id: String) -> Unit,
    private val onDislikeClick: (id: String) -> Unit
) : PagingDataAdapter<T, BaseAdapter<T>.PhotoViewHolder<T>>(PhotoComparator()) {

    inner class PhotoViewHolder<T>(
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

        fun bind(item: T) {
            when (item) {
                is Photo -> {
                    id = item.id

                    Glide.with(itemView)
                        .load(item.url.regular)
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
                is SearchPhoto -> {
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
                else -> error("There is no holder for item $item")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder<T> =
        PhotoViewHolder(
            binding = ItemFeedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onItemClick,
            onLikeClick = onLikeClick,
            onDislikeClick = onDislikeClick
        )

    override fun onBindViewHolder(holder: PhotoViewHolder<T>, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    class PhotoComparator<T : Any> : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
            when {
                oldItem is Photo && newItem is Photo -> oldItem.id == newItem.id
                oldItem is SearchPhoto && newItem is SearchPhoto -> oldItem.id == newItem.id
                else -> error("No such type")
            }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
            when {
                oldItem is Photo && newItem is Photo -> oldItem == newItem
                oldItem is SearchPhoto && newItem is SearchPhoto -> oldItem == newItem
                else -> error("No such type")
            }
    }
}