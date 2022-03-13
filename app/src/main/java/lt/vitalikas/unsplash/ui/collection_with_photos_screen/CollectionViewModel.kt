package lt.vitalikas.unsplash.ui.collection_with_photos_screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatusTracker
import lt.vitalikas.unsplash.data.services.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.LikePhotoWorker
import lt.vitalikas.unsplash.domain.use_cases.GetCollectionPhotosUseCase
import lt.vitalikas.unsplash.domain.use_cases.GetCollectionUseCase
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val context: Application,
    networkStatusTracker: NetworkStatusTracker,
    private val getCollectionUseCase: GetCollectionUseCase,
    private val getCollectionPhotosUseCase: GetCollectionPhotosUseCase
) : ViewModel() {

    val networkStatus = networkStatusTracker.networkStatus

    private val _collectionState = MutableStateFlow<CollectionState>(CollectionState.Init)
    val collectionState = _collectionState.asStateFlow()

    private val _photosState = MutableStateFlow<CollectionPhotosState>(CollectionPhotosState.Init)
    val photosState = _photosState.asStateFlow()

    suspend fun getCollection(id: String) {
        try {
            _collectionState.value = CollectionState.Loading
            val collection = getCollectionUseCase(id)
            _collectionState.value = CollectionState.Success(collection)
        } catch (t: Throwable) {
            _collectionState.value = CollectionState.Error(t)
        }
    }

    suspend fun getCollectionPhotos(id: String) {
        val photos = getCollectionPhotosUseCase(id).cachedIn(viewModelScope)
        photos
            .onEach { pagingData ->
                _photosState.value = CollectionPhotosState.Success(pagingData)
            }
            .catch { t ->
                _photosState.value = CollectionPhotosState.Error(t)
            }
            .launchIn(viewModelScope)
    }

    fun likePhoto(id: String) {
        val workData = workDataOf(
            LikePhotoWorker.LIKE_PHOTO_ID to id
        )

        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(false)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<LikePhotoWorker>()
            .setInputData(workData)
            .setConstraints(workConstraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                LikePhotoWorker.LIKE_PHOTO_WORK_ID_FROM_COLLECTION,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }

    fun dislikePhoto(id: String) {
        val workData = workDataOf(
            DislikePhotoWorker.DISLIKE_PHOTO_ID to id
        )

        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(false)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<DislikePhotoWorker>()
            .setInputData(workData)
            .setConstraints(workConstraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_FROM_COLLECTION,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
    }
}