package lt.vitalikas.unsplash.ui.onboarding_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import lt.vitalikas.unsplash.databinding.ItemOnboardingBinding
import lt.vitalikas.unsplash.domain.models.OnboardingItem

class OnboardingAdapterDelegate :
    AbsListItemAdapterDelegate<OnboardingItem, OnboardingItem, OnboardingAdapterDelegate.OnboardingHolder>() {

    inner class OnboardingHolder(
        private val binding: ItemOnboardingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OnboardingItem) {

        }
    }

    override fun isForViewType(
        item: OnboardingItem,
        items: MutableList<OnboardingItem>,
        position: Int
    ): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): OnboardingHolder =
        OnboardingHolder(
            ItemOnboardingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        item: OnboardingItem,
        holder: OnboardingHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)
}