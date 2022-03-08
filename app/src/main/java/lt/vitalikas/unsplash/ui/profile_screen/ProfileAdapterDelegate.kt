package lt.vitalikas.unsplash.ui.profile_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.ItemPhotoBinding
import lt.vitalikas.unsplash.domain.models.profile.Profile

class ProfileAdapterDelegate :
    AbsListItemAdapterDelegate<Profile.Photo, Profile.Photo, ProfileAdapterDelegate.PhotoViewHolder>() {

    class PhotoViewHolder(
        private val binding: ItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Profile.Photo) {
            Glide.with(itemView)
                .load(item.urls.regular)
                .placeholder(R.drawable.picture)
                .error(R.drawable.picture)
                .into(binding.ivPhoto)
        }
    }

    override fun isForViewType(
        item: Profile.Photo,
        items: MutableList<Profile.Photo>,
        position: Int
    ): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): PhotoViewHolder =
        PhotoViewHolder(
            ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        item: Profile.Photo,
        holder: PhotoViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)
}