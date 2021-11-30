package lt.vitalikas.unsplash.ui.onboarding_screen

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import lt.vitalikas.unsplash.domain.models.OnboardingItem

class OnboardingAdapter : AsyncListDifferDelegationAdapter<OnboardingItem>(
    OnboardingItemDiffUtilCallback()
) {

    init {
        delegatesManager.addDelegate(OnboardingAdapterDelegate())
    }

    class OnboardingItemDiffUtilCallback : DiffUtil.ItemCallback<OnboardingItem>() {

        override fun areItemsTheSame(oldItem: OnboardingItem, newItem: OnboardingItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: OnboardingItem, newItem: OnboardingItem): Boolean =
            oldItem == newItem
    }
}