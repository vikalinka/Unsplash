package lt.vitalikas.unsplash.ui.feed_screen

import androidx.paging.PagingData
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.domain.models.FeedPhoto

sealed class FeedState {
    class Success(val data: PagingData<FeedPhotoEntity>) : FeedState()
    class Error(val error: Throwable) : FeedState()
}