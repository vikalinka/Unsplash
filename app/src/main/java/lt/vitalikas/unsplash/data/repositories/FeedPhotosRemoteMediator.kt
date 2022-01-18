package lt.vitalikas.unsplash.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import lt.vitalikas.unsplash.data.apis.UnsplashApi
import lt.vitalikas.unsplash.data.databases.dao.DatabaseDao
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FeedPhotosRemoteMediator @Inject constructor(
    private val db: DatabaseDao,
    private val api: UnsplashApi
) : RemoteMediator<Int, FeedPhotoEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FeedPhotoEntity>
    ): MediatorResult {
        val loadKey = when(loadType) {
            // For REFRESH, pass null to load the first page.
            LoadType.REFRESH -> null
            // In this example, you never need to prepend, since REFRESH
            // will always load the first page in the list. Immediately
            // return, reporting end of pagination.
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                // You must explicitly check if the last item is null when
                // appending, since passing null to networkService is only
                // valid for initial load. If lastItem is null it means no
                // items were loaded after the initial REFRESH and there are
                // no more items to load.
                if (lastItem == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                lastItem.id
            }
        }

        val response = api.getFeedPhotos(
            loadKey, ORDER_BY
        )
    }

    companion object {
        private const val ORDER_BY = "popular"
    }
}