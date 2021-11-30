package lt.vitalikas.unsplash.domain.models

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingItem(
    val id: Long,
    @StringRes val text: Int,
    @ColorRes val color: Int,
    @DrawableRes val image: Int,
)