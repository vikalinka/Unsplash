package lt.vitalikas.unsplash.ui.onboarding_screen

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class OnboardingTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 || position > 1 -> {
                page.alpha = 0f
            }
            position <= 0 -> {
                page.alpha = 1 + position
            }
            position <= 1 -> {
                page.alpha = 1 - position
            }
        }
    }
}