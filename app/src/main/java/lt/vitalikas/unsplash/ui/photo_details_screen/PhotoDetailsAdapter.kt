package lt.vitalikas.unsplash.ui.photo_details_screen

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import lt.vitalikas.unsplash.domain.models.photo_details.PhotoDetails

class PhotoDetailsAdapter(
    onLocationClick: (lat: Double, lng: Double) -> Unit,
    onDownloadClick: (url: String) -> Unit
) : AsyncListDifferDelegationAdapter<PhotoDetails>(FeedPhotoDetailsDiffUtilItemCallback()) {

    init {
        delegatesManager.addDelegate(
            PhotoDetailsAdapterDelegate(
                onLocationClick = onLocationClick,
                onDownloadClick = onDownloadClick
            )
        )
    }

    class FeedPhotoDetailsDiffUtilItemCallback : DiffUtil.ItemCallback<PhotoDetails>() {
        override fun areItemsTheSame(
            oldItem: PhotoDetails,
            newItem: PhotoDetails
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PhotoDetails,
            newItem: PhotoDetails
        ): Boolean = oldItem == newItem
    }
}