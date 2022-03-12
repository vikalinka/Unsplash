package lt.vitalikas.unsplash.ui.collections_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.ItemCollectionBinding
import lt.vitalikas.unsplash.domain.models.collections.CollectionResponse

class CollectionsAdapter(
    private val onItemClick: (id: String) -> Unit
) : PagingDataAdapter<CollectionResponse, CollectionsAdapter.CollectionResponseViewHolder>(
    CollectionResponseComparator()
) {

    inner class CollectionResponseViewHolder(
        private val binding: ItemCollectionBinding,
        onItemClick: (id: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var id: String

        init {
            binding.root.setOnClickListener {
                onItemClick(id)
            }
        }

        fun bind(item: CollectionResponse) {
            id = item.id

            Glide.with(itemView)
                .load(item.coverPhoto.urls.regular)
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(binding.ivPhoto)

            Glide.with(itemView)
                .load(item.user.profileImage.medium)
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(binding.userImageView)

            binding.nameTextView.text = item.user.name
            binding.usernameTextView.text =
                itemView.resources.getString(R.string.username, item.user.username)
            binding.collectionTitleTextView.text = item.title
            binding.photoCountTextView.text = item.totalPhotos.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionResponseViewHolder =
        CollectionResponseViewHolder(
            binding = ItemCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onItemClick
        )

    override fun onBindViewHolder(holder: CollectionResponseViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    class CollectionResponseComparator : DiffUtil.ItemCallback<CollectionResponse>() {
        override fun areItemsTheSame(
            oldItem: CollectionResponse,
            newItem: CollectionResponse
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CollectionResponse,
            newItem: CollectionResponse
        ): Boolean = oldItem == newItem
    }
}