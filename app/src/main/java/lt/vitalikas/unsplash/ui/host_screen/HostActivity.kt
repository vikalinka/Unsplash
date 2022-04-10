package lt.vitalikas.unsplash.ui.host_screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.ui.feed_screen.FeedViewModel
import lt.vitalikas.unsplash.ui.feed_screen.PhotoWithPermissionDownloader
import lt.vitalikas.unsplash.ui.photo_details_screen.PhotoDetailsFragmentDirections
import lt.vitalikas.unsplash.utils.hasQ
import lt.vitalikas.unsplash.utils.showInfo

@AndroidEntryPoint
class HostActivity : AppCompatActivity(R.layout.activity_host), PhotoWithPermissionDownloader {

    private val feedViewModel by viewModels<FeedViewModel>()

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcvNavHostFragment)
                ?: error("Fragment not found")

        navHostFragment.findNavController()
    }

    private lateinit var photoId: String
    private lateinit var photoDownloadUrl: String

    private val onPhotoDownloadCallback: OnPhotoDownloadCallback
        get() = run {
            val navHostFragment = supportFragmentManager.primaryNavigationFragment
                ?: error("NavHost fragment not found")
            val loggedFragment = navHostFragment.childFragmentManager.primaryNavigationFragment
                ?: error("Logged fragment not found")
            val navHostFrg = loggedFragment.childFragmentManager.primaryNavigationFragment
                ?: error("NavHost fragment not found")
            val currentFragment = navHostFrg.childFragmentManager.fragments.first()
                ?: error("Current fragment not found")

            currentFragment as OnPhotoDownloadCallback
        }

    private fun hasAllPermissions(): Boolean = PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() =
        requestPermissionLauncher.launch(PERMISSIONS.toTypedArray())

    private fun isNeedToShowRationale(): Boolean = PERMISSIONS.any { permission ->
        isNeedRationaleForPermission(permission)
    }

    private fun isNeedRationaleForPermission(permission: String) =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            permission
        )

    private fun showPermissionRationaleDialog() =
        navController.navigate(PhotoDetailsFragmentDirections.actionDetails1ToRationale1())

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsWithStatuses ->
        if (permissionsWithStatuses.values.all { isGranted ->
                isGranted
            }) {
            savePhotoInSelectedFolderLauncher.launch("${photoId}.jpg")
        } else {
            if (isNeedToShowRationale()) {
                showPermissionRationaleDialog()
            } else {
                showInfo(R.string.perm_all)
            }
        }
    }

    private val savePhotoInSelectedFolderLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument(MIME_TYPE)
    ) { uri ->
        uri?.let {
            onPhotoDownloadCallback.getLocationUri(it)
            feedViewModel.downloadPhoto(photoDownloadUrl, it)
        }
    }

    override fun fetchPhotoWithPermission(photoId: String, photoDownloadUrl: String) {
        this.photoId = photoId
        this.photoDownloadUrl = photoDownloadUrl

        if (hasAllPermissions().not()) {
            requestPermissions()
        } else {
            savePhotoInSelectedFolderLauncher.launch("${photoId}.jpg")
        }
    }

    private companion object {
        val PERMISSIONS = listOfNotNull(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE.takeIf {
                hasQ().not()
            }
        )

        const val MIME_TYPE = "image/jpeg"
    }
}