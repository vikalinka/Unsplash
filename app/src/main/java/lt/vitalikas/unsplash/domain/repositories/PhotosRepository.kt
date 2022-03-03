package lt.vitalikas.unsplash.domain.repositories

import android.net.Uri
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.data.db.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails
import lt.vitalikas.unsplash.domain.models.collections.Collection
import lt.vitalikas.unsplash.domain.models.collections.CollectionPhoto
import lt.vitalikas.unsplash.domain.models.collections.CollectionResponse
import lt.vitalikas.unsplash.domain.models.search.SearchPhoto

interface PhotosRepository {
    suspend fun getFeedPhotos(): Flow<PagingData<FeedPhoto>>
    suspend fun getFeedPhotoDetailsById(id: String): FeedPhotoDetails
    suspend fun insertFeedPhotos(feedPhotos: List<FeedPhotoEntity>)
    suspend fun downloadPhoto(url: String, uri: Uri)
    suspend fun likePhoto(id: String)
    suspend fun dislikePhoto(id: String)
    suspend fun updatePhoto(id: String, isLiked: Boolean, likeCount: Int)
    fun getSearchResult(query: String): Flow<PagingData<SearchPhoto>>
    fun getCollections(): Flow<PagingData<CollectionResponse>>
    suspend fun getCollection(id: String): Collection
    fun getCollectionPhotos(id: String): Flow<PagingData<CollectionPhoto>>
}