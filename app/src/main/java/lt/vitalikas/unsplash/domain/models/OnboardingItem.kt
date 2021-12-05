package lt.vitalikas.unsplash.domain.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingItem(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val text: Int
)