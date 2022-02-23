package lt.vitalikas.unsplash.utils

import android.os.Build
import android.view.View
import android.widget.SearchView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lt.vitalikas.unsplash.R

fun hasQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun showInfo(view: View, message: String) {
    Snackbar
        .make(
            view,
            message,
            Snackbar.LENGTH_LONG
        )
        .setAnchorView(R.id.bottom_navigation)
        .show()
}

fun showInfo(view: View, @StringRes message: Int) {
    Snackbar
        .make(
            view,
            message,
            Snackbar.LENGTH_LONG
        )
        .setAnchorView(R.id.bottom_navigation)
        .show()
}

fun showInfoWithAction(
    view: View,
    @StringRes message: Int,
    @StringRes actionText: Int,
    action: () -> Unit
) {
    Snackbar
        .make(
            view,
            message,
            Snackbar.LENGTH_LONG
        )
        .setAnchorView(R.id.bottom_navigation)
        .setAction(actionText) {
            action()
        }
        .show()
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