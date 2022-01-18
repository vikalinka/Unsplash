package lt.vitalikas.unsplash.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import lt.vitalikas.unsplash.data.apis.UnsplashApi
import lt.vitalikas.unsplash.data.databases.Database
import lt.vitalikas.unsplash.data.databases.dao.DatabaseDao
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FeedPhotosRemoteMediator @Inject constructor(
    private val api: UnsplashApi
) : RemoteMediator<Int, FeedPhotoEntity>() {

    private val feedPhotosDao = Database.instance.feedPhotosDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FeedPhotoEntity>
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                lastItem.id
            }
        }

        val response = api.getFeedPhotos(

        )
    }

    companion object {
        private const val ORDER_BY = "popular"
    }
}