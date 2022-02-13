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
class DownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    val downloadPhotoUseCase: DownloadPhotoUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val url = inputData.getString(DOWNLOAD_PHOTO_URL_KEY).orEmpty()
        val uri = Uri.parse(inputData.getString(DOWNLOAD_PHOTO_URI_KEY).orEmpty())

        return try {
            downloadPhotoUseCase(url, uri)
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {
        const val DOWNLOAD_PHOTO_URL_KEY = "url"
        const val DOWNLOAD_PHOTO_URI_KEY = "uri"
        const val DOWNLOAD_PHOTO_WORK_UNIQUE_ID = "id"
    }
}