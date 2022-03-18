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

        val id = inputData.getString(PHOTO_ID).orEmpty()

        return try {
            likePhotoUseCase(id)
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {

        const val PHOTO_ID = "photo_id"
        const val LIKE_PHOTO_WORK_ID_FEED = "like_photo_work_id_feed"
        const val LIKE_PHOTO_WORK_ID_DETAILS = "like_photo_work_id_details"
        const val LIKE_PHOTO_WORK_ID_COLLECTION = "like_photo_work_id_collection"

        fun likePhotoRequest(photoId: String) = OneTimeWorkRequestBuilder<LikePhotoWorker>()
            .setInputData(workDataOf(PHOTO_ID to photoId))
            .setConstraints(PhotoOperationsConstraints.ReactionConstraints.constraints)
            .build()
    }
}