package lt.vitalikas.unsplash.data.services

import android.net.Uri
import androidx.work.*

class DownloadRequest(
    url: String,
    uri: Uri
) {

    private val workData = workDataOf(
        DownloadWorker.DOWNLOAD_PHOTO_URL_KEY to url,
        DownloadWorker.DOWNLOAD_PHOTO_URI_KEY to uri.toString()
    )

    private val workConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresStorageNotLow(true)
        .build()

    val workRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
        .setInputData(workData)
        .setConstraints(workConstraints)
        .build()
}