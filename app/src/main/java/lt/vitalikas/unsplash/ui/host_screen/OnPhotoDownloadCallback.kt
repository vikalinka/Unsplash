package lt.vitalikas.unsplash.ui.host_screen

import android.net.Uri

interface OnPhotoDownloadCallback {

    fun getLocationUri(uri: Uri)
}