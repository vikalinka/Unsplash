package lt.vitalikas.unsplash.data.services.photo_service

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
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
        val uri = Uri.parse(inputData.getString(FETCHING_LOCATION_URI).orEmpty())

        return try {
            downloadPhotoUseCase(url, uri)
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {

        const val PHOTO_ID = "photo_id"
        const val FETCHING_LOCATION_URI = "fetching_location_uri"
        const val DOWNLOAD_PHOTO_WORK_ID = "download_photo_work_id"

        fun downloadPhotoRequest(
            photoId: String,
            fetchingLocationUri: Uri
        ) = OneTimeWorkRequestBuilder<DownloadPhotoWorker>()
            .setInputData(
                workDataOf(
                    PHOTO_ID to photoId,
                    FETCHING_LOCATION_URI to fetchingLocationUri.toString()
                )
            )
            .setConstraints(PhotoOperationsConstraints.FetchingConstraints.constraints)
            .build()
    }
}