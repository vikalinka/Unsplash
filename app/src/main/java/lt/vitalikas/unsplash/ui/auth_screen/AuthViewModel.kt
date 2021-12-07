package lt.vitalikas.unsplash.ui.auth_screen

import android.app.Application
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.AndroidViewModel
import lt.vitalikas.unsplash.R

class AuthViewModel(private val app: Application) : AndroidViewModel(app) {

    fun openLoginPage() {
        val colorScheme = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(getColor(app, R.color.purple_200))
            .build()

        val customTabsIntent = CustomTabsIntent.Builder()
            .setToolbarColor(ContextCompat.getColor(getApplication(), R.color.purple_200))
            .build()
    }
}