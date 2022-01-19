package lt.vitalikas.unsplash.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.data.apis.UnsplashApi
import lt.vitalikas.unsplash.data.databases.dao.FeedPhotosDao
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails
import lt.vitalikas.unsplash.domain.repositories.FeedPhotosRepository
import javax.inject.Inject

class FeedPhotosRepositoryImpl @Inject constructor(
    private val api: UnsplashApi,
    private val dao: FeedPhotosDao
) : FeedPhotosRepository {

    @ExperimentalPagingApi
    override suspend fun getFeedPhotos(): Flow<PagingData<FeedPhotoEntity>> {
        val pagingSourceFactory = {
            dao.getPagingSource()
        }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = FeedPhotosRemoteMediator(api),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }


    override suspend fun getFeedPhotoDetailsById(id: String): FeedPhotoDetails =
        api.getFeedPhotoDetails(id)

    override suspend fun insertFeedPhotos(feedPhotos: List<FeedPhotoEntity>) =
        dao.insertAllFeedPhotos(feedPhotos)

    companion object {
        private const val PAGE_SIZE = 10
    }
}