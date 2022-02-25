package lt.vitalikas.unsplash.utils

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.SearchView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import lt.vitalikas.unsplash.R
import timber.log.Timber

fun hasQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

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