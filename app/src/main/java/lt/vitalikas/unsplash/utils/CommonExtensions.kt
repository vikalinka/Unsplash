package lt.vitalikas.unsplash.utils

import android.os.Build
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import lt.vitalikas.unsplash.R

fun hasQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun showInfo(view: View, message: String) {
    Snackbar
        .make(
            view,
            message,
            Snackbar.LENGTH_LONG
        )
        .show()
}

fun showInfo(view: View, @StringRes message: Int) {
    Snackbar
        .make(
            view,
            message,
            Snackbar.LENGTH_LONG
        )
        .show()
}

fun showInfoWithAction(view: View, @StringRes message: Int, action: () -> Unit) {
    Snackbar
        .make(
            view,
            message,
            Snackbar.LENGTH_LONG
        )
        .setAnchorView(R.id.bottom_navigation)
        .setAction("Open") {
            action()
        }
        .show()
}