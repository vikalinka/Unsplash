package lt.vitalikas.unsplash.data.services

import androidx.work.*

class DownloadRequest(
    url: String
) {

    private val workData = workDataOf(
        DownloadWorker.DOWNLOAD_PHOTO_WORK_KEY to url
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