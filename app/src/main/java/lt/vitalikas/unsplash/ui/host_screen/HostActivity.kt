package lt.vitalikas.unsplash.ui.host_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import lt.vitalikas.unsplash.R

@AndroidEntryPoint
class HostActivity : AppCompatActivity(R.layout.activity_host) {

    // https://api.unsplash.com/photos/4oovIxttThA
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        findNavController(R.id.app_nav)
    }
}