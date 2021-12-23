package lt.vitalikas.unsplash.ui.profile_screen

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import lt.vitalikas.unsplash.domain.models.Profile

class ProfileAdapter :
    AsyncListDifferDelegationAdapter<Profile.Photo>(PhotoItemDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(ProfileAdapterDelegate())
    }

    class PhotoItemDiffUtilCallback : DiffUtil.ItemCallback<Profile.Photo>() {
        override fun areItemsTheSame(oldItem: Profile.Photo, newItem: Profile.Photo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Profile.Photo, newItem: Profile.Photo): Boolean =
            oldItem == newItem
    }
}