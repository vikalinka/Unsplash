package lt.vitalikas.unsplash.data.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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
        val id = inputData.getString(LIKE_PHOTO_ID).orEmpty()

        return try {
            likePhotoUseCase(id)
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {
        const val LIKE_PHOTO_ID = "like_photo_id"
        const val LIKE_PHOTO_WORK_ID = "like_photo_work_id"
    }
}