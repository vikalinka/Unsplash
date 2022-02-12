package lt.vitalikas.unsplash.data.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import lt.vitalikas.unsplash.domain.use_cases.DownloadPhotoUseCase

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val downloadPhotoUseCase: DownloadPhotoUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val url = inputData.getString(DOWNLOAD_PHOTO_WORK_KEY).orEmpty()

        return try {
            downloadPhotoUseCase(url)
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {
        const val DOWNLOAD_PHOTO_WORK_KEY = "download_photo_work_key"
        const val DOWNLOAD_PHOTO_WORK_UNIQUE_ID = "download_photo_work_key"
    }
}