package lt.vitalikas.unsplash.data.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import lt.vitalikas.unsplash.domain.use_cases.LikePhotoUseCase

@HiltWorker
class LikePhotoWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val likePhotoUseCase: LikePhotoUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val id = inputData.getString(PHOTO_ID_KEY).orEmpty()

        return try {
            likePhotoUseCase(id)
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {
        const val PHOTO_ID_KEY = "photo_id_key"
        const val LIKE_PHOTO_WORK_ID_FROM_FEED = "like_photo_work_id_from_feed"
        const val LIKE_PHOTO_WORK_ID_FROM_DETAILS = "like_photo_work_id_from_details"
        const val LIKE_PHOTO_WORK_ID_FROM_COLLECTION = "like_photo_work_id_from_collection"

        private val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(false)
            .build()

        fun makeRequest(id: String) = OneTimeWorkRequestBuilder<LikePhotoWorker>()
            .setInputData(
                workDataOf(PHOTO_ID_KEY to id)
            )
            .setConstraints(workConstraints)
            .build()
    }
}