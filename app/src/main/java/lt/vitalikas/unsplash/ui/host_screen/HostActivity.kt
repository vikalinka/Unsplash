package lt.vitalikas.unsplash.ui.host_screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import lt.vitalikas.unsplash.R

@AndroidEntryPoint
class HostActivity : AppCompatActivity(R.layout.activity_host) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAppFirstBoot()
    }

    private fun checkAppFirstBoot() {
        val sharedPrefs = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val isFirstBoot = sharedPrefs.getBoolean("isFirstBoot", true)

        if (isFirstBoot) {
            sharedPrefs.edit().putBoolean("isFirstBoot", false)
                .apply()

            startActivity(Intent(this@HostActivity, HostActivity::class.java))
        } else {
            finish()
        }
    }
}