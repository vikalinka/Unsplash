package lt.vitalikas.unsplash.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.data.apis.UnsplashApi
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import javax.inject.Inject

class FeedPhotosRepositoryImpl @Inject constructor(
    private val api: UnsplashApi
) : FeedPhotosRepository {

    override suspend fun getFeedPhotos(): Flow<PagingData<FeedPhoto>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                FeedPhotosPagingSource(api)
            }
        ).flow
    }


    override suspend fun getFeedPhotoDetailsById(id: String): FeedPhotoDetails =
        api.getFeedPhotoDetails(id)

    companion object {
        private const val PAGE_SIZE = 15
    }
}