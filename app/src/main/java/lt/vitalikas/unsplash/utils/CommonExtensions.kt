package lt.vitalikas.unsplash.utils

import android.app.Activity
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.SearchView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.services.photo_service.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.photo_service.DownloadPhotoWorker
import lt.vitalikas.unsplash.data.services.photo_service.LikePhotoWorker
import lt.vitalikas.unsplash.data.services.photo_service.WorkType
import timber.log.Timber

fun hasQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun Activity.showInfo(@StringRes message: Int) {
    Snackbar
        .make(
            findViewById(R.id.hostActivity),
            message,
            Snackbar.LENGTH_LONG
        )
        .setAnchorView(R.id.bottom_navigation)
        .show()
}

fun Fragment.showInfo(message: String) {
    Snackbar
        .make(
            this.requireView(),
            message,
            Snackbar.LENGTH_LONG
        )
        .setAnchorView(R.id.bottom_navigation)
        .show()
}

fun Fragment.showInfo(@StringRes message: Int) {
    Snackbar
        .make(
            this.requireView(),
            message,
            Snackbar.LENGTH_LONG
        )
        .setAnchorView(R.id.bottom_navigation)
        .show()
}

fun Fragment.showInfoWithAction(
    @StringRes message: Int,
    @StringRes actionText: Int,
    action: () -> Unit
) {
    Snackbar
        .make(
            this.requireView(),
            message,
            Snackbar.LENGTH_LONG
        )
        .setAnchorView(R.id.bottom_navigation)
        .setAction(actionText) {
            action()
        }
        .show()
}

fun Fragment.observeWorks(
    workIds: List<String>,
    callbackOnWorkFinish: (type: WorkType) -> Unit
) {
    workIds.forEach { workId ->
        when (workId) {
            LikePhotoWorker.LIKE_PHOTO_WORK_ID_FEED,
            LikePhotoWorker.LIKE_PHOTO_WORK_ID_DETAILS,
            LikePhotoWorker.LIKE_PHOTO_WORK_ID_COLLECTION -> {
                WorkManager.getInstance(requireContext())
                    .getWorkInfosForUniqueWorkLiveData(workId)
                    .observe(viewLifecycleOwner) { workInfos ->
                        if (workInfos.isNullOrEmpty()) {
                            return@observe
                        }
                        when (workInfos.first().state) {
                            WorkInfo.State.ENQUEUED -> {
                                Timber.d("LIKING PHOTO ENQUEUED")
                            }
                            WorkInfo.State.RUNNING -> {
                                Timber.d("LIKING PHOTO RUNNING")
                            }
                            WorkInfo.State.FAILED -> {
                                Timber.d("LIKING PHOTO FAILED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Timber.d("LIKING PHOTO SUCCEEDED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                                callbackOnWorkFinish(WorkType.Reaction(true))
                            }
                            WorkInfo.State.CANCELLED -> {
                                Timber.d("LIKING PHOTO CANCELED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.BLOCKED -> {
                                Timber.d("LIKING PHOTO BLOCKED")
                            }
                        }
                    }
            }
            DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_FEED,
            DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_DETAILS,
            DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_COLLECTION -> {
                WorkManager.getInstance(requireContext())
                    .getWorkInfosForUniqueWorkLiveData(workId)
                    .observe(viewLifecycleOwner) { workInfos ->
                        if (workInfos.isNullOrEmpty()) {
                            return@observe
                        }
                        when (workInfos.first().state) {
                            WorkInfo.State.ENQUEUED -> {
                                Timber.d("DISLIKING PHOTO ENQUEUED")
                            }
                            WorkInfo.State.RUNNING -> {
                                Timber.d("DISLIKING PHOTO RUNNING")
                            }
                            WorkInfo.State.FAILED -> {
                                Timber.d("DISLIKING PHOTO FAILED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Timber.d("DISLIKING PHOTO SUCCEEDED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                                callbackOnWorkFinish(WorkType.Reaction(false))
                            }
                            WorkInfo.State.CANCELLED -> {
                                Timber.d("DISLIKING PHOTO CANCELED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.BLOCKED -> {
                                Timber.d("DISLIKING PHOTO BLOCKED")
                            }
                        }
                    }
            }
            DownloadPhotoWorker.DOWNLOAD_PHOTO_WORK_ID -> {
                WorkManager.getInstance(requireContext())
                    .getWorkInfosForUniqueWorkLiveData(DownloadPhotoWorker.DOWNLOAD_PHOTO_WORK_ID)
                    .observe(viewLifecycleOwner) { workInfos ->
                        if (workInfos.isNullOrEmpty()) {
                            return@observe
                        }
                        when (workInfos.first().state) {
                            WorkInfo.State.ENQUEUED -> {
                                Timber.d("DOWNLOAD ENQUEUED")
                            }
                            WorkInfo.State.RUNNING -> {
                                Timber.d("DOWNLOAD RUNNING")
                            }
                            WorkInfo.State.FAILED -> {
                                Timber.d("DOWNLOAD FAILED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Timber.d("DOWNLOAD SUCCEEDED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                                callbackOnWorkFinish(WorkType.Download)
                            }
                            WorkInfo.State.CANCELLED -> {
                                Timber.d("DOWNLOAD CANCELED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.BLOCKED -> {
                                Timber.d("DOWNLOAD BLOCKED")
                            }
                        }
                    }
            }
            else -> error("No handler for work id = $workId")
        }
    }
}

fun Fragment.observePhotoReaction(
    workIds: List<String>,
    callbackOnReaction: (reaction: Boolean) -> Unit
) {
    workIds.forEach { workId ->
        when (workId) {
            LikePhotoWorker.LIKE_PHOTO_WORK_ID_FEED,
            LikePhotoWorker.LIKE_PHOTO_WORK_ID_DETAILS,
            LikePhotoWorker.LIKE_PHOTO_WORK_ID_COLLECTION -> {
                WorkManager.getInstance(requireContext())
                    .getWorkInfosForUniqueWorkLiveData(workId)
                    .observe(viewLifecycleOwner) { workInfos ->
                        if (workInfos.isNullOrEmpty()) {
                            return@observe
                        }
                        when (workInfos.first().state) {
                            WorkInfo.State.ENQUEUED -> {
                                Timber.d("LIKING PHOTO ENQUEUED")
                            }
                            WorkInfo.State.RUNNING -> {
                                Timber.d("LIKING PHOTO RUNNING")
                            }
                            WorkInfo.State.FAILED -> {
                                Timber.d("LIKING PHOTO FAILED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Timber.d("LIKING PHOTO SUCCEEDED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                                callbackOnReaction(true)
                            }
                            WorkInfo.State.CANCELLED -> {
                                Timber.d("LIKING PHOTO CANCELED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.BLOCKED -> {
                                Timber.d("LIKING PHOTO BLOCKED")
                            }
                        }
                    }
            }
            DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_FEED,
            DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_DETAILS,
            DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_COLLECTION -> {
                WorkManager.getInstance(requireContext())
                    .getWorkInfosForUniqueWorkLiveData(workId)
                    .observe(viewLifecycleOwner) { workInfos ->
                        if (workInfos.isNullOrEmpty()) {
                            return@observe
                        }
                        when (workInfos.first().state) {
                            WorkInfo.State.ENQUEUED -> {
                                Timber.d("DISLIKING PHOTO ENQUEUED")
                            }
                            WorkInfo.State.RUNNING -> {
                                Timber.d("DISLIKING PHOTO RUNNING")
                            }
                            WorkInfo.State.FAILED -> {
                                Timber.d("DISLIKING PHOTO FAILED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Timber.d("DISLIKING PHOTO SUCCEEDED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                                callbackOnReaction(false)
                            }
                            WorkInfo.State.CANCELLED -> {
                                Timber.d("DISLIKING PHOTO CANCELED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.BLOCKED -> {
                                Timber.d("DISLIKING PHOTO BLOCKED")
                            }
                        }
                    }
            }
            else -> error("No handler for work id = $workId")
        }
    }
}


fun Fragment.observePhotoDownload(
    workIds: List<String>,
    callbackOnPhotoDownload: () -> Unit
) {
    workIds.forEach { workId ->
        when (workId) {
            DownloadPhotoWorker.DOWNLOAD_PHOTO_WORK_ID -> {
                WorkManager.getInstance(requireContext())
                    .getWorkInfosForUniqueWorkLiveData(DownloadPhotoWorker.DOWNLOAD_PHOTO_WORK_ID)
                    .observe(viewLifecycleOwner) { workInfos ->
                        if (workInfos.isNullOrEmpty()) {
                            return@observe
                        }
                        when (workInfos.first().state) {
                            WorkInfo.State.ENQUEUED -> {
                                Timber.d("DOWNLOAD ENQUEUED")
                            }
                            WorkInfo.State.RUNNING -> {
                                Timber.d("DOWNLOAD RUNNING")
                            }
                            WorkInfo.State.FAILED -> {
                                Timber.d("DOWNLOAD FAILED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Timber.d("DOWNLOAD SUCCEEDED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                                callbackOnPhotoDownload()
                            }
                            WorkInfo.State.CANCELLED -> {
                                Timber.d("DOWNLOAD CANCELED")
                                WorkManager.getInstance(requireContext()).pruneWork()
                            }
                            WorkInfo.State.BLOCKED -> {
                                Timber.d("DOWNLOAD BLOCKED")
                            }
                        }
                    }
            }
        }
    }
}

fun SearchView.onTextChangedFlow(): Flow<String> {
    var listener: SearchView.OnQueryTextListener?

    return callbackFlow {
        listener = object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let { trySend(it) }
                return true
            }
        }

        this@onTextChangedFlow.setOnQueryTextListener(listener)

        awaitClose {
            listener = null
        }
    }
}

fun TextInputEditText.onTextChangedFlow(): Flow<String> {
    return callbackFlow<String> {

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int) {
                trySendBlocking(cs?.toString().orEmpty())
                    .onSuccess {

                    }
                    .onFailure { t: Throwable? ->
                        Timber.d(t)
                    }
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        this@onTextChangedFlow.addTextChangedListener(watcher)

        awaitClose {
            this@onTextChangedFlow.removeTextChangedListener(watcher)
        }
    }
}