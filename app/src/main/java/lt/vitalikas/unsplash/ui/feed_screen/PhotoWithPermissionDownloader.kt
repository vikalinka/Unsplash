package lt.vitalikas.unsplash.ui.feed_screen

interface PhotoWithPermissionDownloader {

    fun fetchPhotoWithPermission(photoId: String, photoDownloadUrl: String)
}