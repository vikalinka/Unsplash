package lt.vitalikas.unsplash.data.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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

        val id = inputData.getString(DISLIKE_PHOTO_ID).orEmpty()

        return try {
            dislikePhotoUseCase(id)
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {
        const val DISLIKE_PHOTO_ID = "dislike_photo_id"
        const val DISLIKE_PHOTO_WORK_ID_FROM_DETAILS = "dislike_photo_work_id_from_details"
        const val DISLIKE_PHOTO_WORK_ID_FROM_FEED = "dislike_photo_work_id_from_feed"
    }
}