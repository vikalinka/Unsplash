package lt.vitalikas.unsplash.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import lt.vitalikas.unsplash.ui.feed_details_screen.FeedDetailsFragment
import javax.inject.Inject

class Perm(val activity: FragmentActivity) {
    private val _state =
        MutableStateFlow<PermStatus>(PermStatus.NotGranted)
    val state = _state.asStateFlow()

    private val requestPermissionLauncher =

            activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissionsWithStatuses ->
                if (permissionsWithStatuses.values.all { isGranted ->
                        isGranted == true
                    }) {
                    _state.value = PermStatus.Granted
                } else {
                    if (isNeedToShowRationale()) {
                        _state.value = PermStatus.NeedRationale
                    } else {
                        _state.value = PermStatus.NeedCheckSettings
                    }
                }
            }

    fun checkPermissions() {
        if (hasAllPermissions().not()) {
            requestPermissions()
        } else {
            _state.value = PermStatus.Granted
        }
    }

    private fun hasAllPermissions(): Boolean = PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(
            activity.baseContext,
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
            activity,
            permission
        )

    companion object {
        private val PERMISSIONS = listOfNotNull(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE.takeIf {
                hasQ().not()
            }
        )
    }
}