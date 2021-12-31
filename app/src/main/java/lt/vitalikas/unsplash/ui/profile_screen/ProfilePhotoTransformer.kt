package lt.vitalikas.unsplash.ui.profile_screen

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class ProfilePhotoTransformer : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> page.alpha = 0f
            position <= 0 -> {
                with(page) {
                    alpha = 1f
                    pivotX = page.width.toFloat()
                    rotationY = -90 * abs(position)
                }
            }
            position <= 1 -> {
                with(page) {
                    alpha = 1f
                    pivotX = 0f
                    rotationY = 90 * abs(position)
                }
            }
            else -> page.alpha = 0f
        }
    }
}