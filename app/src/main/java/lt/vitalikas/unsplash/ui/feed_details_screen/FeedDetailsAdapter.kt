package lt.vitalikas.unsplash.ui.feed_details_screen

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import lt.vitalikas.unsplash.domain.models.photo_details.PhotoDetails

class FeedDetailsAdapter(
    onLocationClick: (lat: Double, lng: Double) -> Unit,
    onDownloadClick: (url: String) -> Unit
) : AsyncListDifferDelegationAdapter<PhotoDetails>(FeedPhotoDetailsDiffUtilItemCallback()) {

    init {
        delegatesManager.addDelegate(
            FeedDetailsAdapterDelegate(
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