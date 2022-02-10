package lt.vitalikas.unsplash.ui.feed_details_screen

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails

class FeedDetailsAdapter(
    onLocationClick: (lat: Double, lng: Double) -> Unit,
    onDownloadClick: (link: String) -> Unit
) : AsyncListDifferDelegationAdapter<FeedPhotoDetails>(FeedPhotoDetailsDiffUtilItemCallback()) {

    init {
        delegatesManager.addDelegate(
            FeedDetailsAdapterDelegate(
                onLocationClick = onLocationClick,
                onDownloadClick = onDownloadClick
            )
        )
    }

    class FeedPhotoDetailsDiffUtilItemCallback : DiffUtil.ItemCallback<FeedPhotoDetails>() {
        override fun areItemsTheSame(
            oldItem: FeedPhotoDetails,
            newItem: FeedPhotoDetails
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: FeedPhotoDetails,
            newItem: FeedPhotoDetails
        ): Boolean = oldItem == newItem
    }
}