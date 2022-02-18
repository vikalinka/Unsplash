package lt.vitalikas.unsplash.data.services

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import lt.vitalikas.unsplash.domain.use_cases.DownloadPhotoUseCase

@HiltWorker
class DownloadPhotoWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val downloadPhotoUseCase: DownloadPhotoUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val url = inputData.getString(PHOTO_ID).orEmpty()
        val uri = Uri.parse(inputData.getString(PHOTO_URI).orEmpty())

        return try {
            downloadPhotoUseCase(url, uri)
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {
        const val PHOTO_ID = "photo_id"
        const val PHOTO_URI = "photo_uri"
        const val DOWNLOAD_PHOTO_WORK_ID = "download_photo_work_id"
    }
}