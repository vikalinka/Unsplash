package lt.vitalikas.unsplash.data.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import lt.vitalikas.unsplash.domain.use_cases.DislikePhotoUseCase

@HiltWorker
class DislikePhotoWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val dislikePhotoUseCase: DislikePhotoUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val id = inputData.getString(PHOTO_ID).orEmpty()

        return try {
            dislikePhotoUseCase(id)
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {

        const val PHOTO_ID = "photo_id"
        const val DISLIKE_PHOTO_WORK_ID_FEED = "dislike_photo_work_id_feed"
        const val DISLIKE_PHOTO_WORK_ID_DETAILS = "dislike_photo_work_id_details"
        const val DISLIKE_PHOTO_WORK_ID_COLLECTION = "dislike_photo_work_id_collection"

        fun dislikePhotoRequest(photoId: String) = OneTimeWorkRequestBuilder<DislikePhotoWorker>()
            .setInputData(workDataOf(PHOTO_ID to photoId))
            .setConstraints(PhotoOperationsConstraints.ReactionConstraints.constraints)
            .build()
    }
}