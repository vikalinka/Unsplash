package lt.vitalikas.unsplash.domain.repositories

import android.net.Uri
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import lt.vitalikas.unsplash.data.db.entities.PhotoEntity
import lt.vitalikas.unsplash.domain.models.photo_details.PhotoDetails
import lt.vitalikas.unsplash.domain.models.collections.Collection
import lt.vitalikas.unsplash.domain.models.collections.CollectionPhoto
import lt.vitalikas.unsplash.domain.models.collections.CollectionResponse
import lt.vitalikas.unsplash.domain.models.photo.Photo

interface PhotosRepository {
    suspend fun getPhotos(order: String, currentOrder: String): Flow<PagingData<Photo>>
    suspend fun getFeedPhotoDetailsById(id: String): PhotoDetails
    suspend fun insertAllPhotos(photos: List<PhotoEntity>)
    suspend fun downloadPhoto(url: String, uri: Uri)
    suspend fun likePhoto(id: String)
    suspend fun dislikePhoto(id: String)
    suspend fun updatePhoto(id: String, isLiked: Boolean, likeCount: Int)
    fun getSearchResult(query: String): Flow<PagingData<Photo>>
    fun getCollections(): Flow<PagingData<CollectionResponse>>
    suspend fun getCollection(id: String): Collection
    fun getCollectionPhotos(id: String): Flow<PagingData<CollectionPhoto>>
}