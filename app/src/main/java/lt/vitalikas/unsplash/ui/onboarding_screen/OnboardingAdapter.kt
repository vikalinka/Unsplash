package lt.vitalikas.unsplash.ui.onboarding_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lt.vitalikas.unsplash.databinding.ItemOnboardingBinding
import lt.vitalikas.unsplash.domain.models.onboarding.OnboardingItem

class OnboardingAdapter(
    private val items: List<OnboardingItem>,
    private val onActionGetStartedClick: () -> Unit,
    private val onActionSkipClick: () -> Unit,
    private val onActionPrevClick: () -> Unit,
    private val onActionNextClick: () -> Unit
) : RecyclerView.Adapter<OnboardingAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(
            binding = ItemOnboardingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onActionGetStartedClick = onActionGetStartedClick,
            onActionSkipClick = onActionSkipClick,
            onActionPrevClick = onActionPrevClick,
            onActionNextClick = onActionNextClick
        )

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class Holder(
        private val binding: ItemOnboardingBinding,
        private val onActionGetStartedClick: () -> Unit,
        private val onActionSkipClick: () -> Unit,
        private val onActionPrevClick: () -> Unit,
        private val onActionNextClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OnboardingItem) {
            Glide.with(itemView)
                .load(item.image)
                .into(binding.onboardingImageView)

            binding.onboardingTitleTextView.text = itemView.resources.getText(item.title)
            binding.onboardingDescriptionTextView.text = itemView.resources.getText(item.text)

            with(binding.getStartedActionMaterialButton) {
                setOnClickListener { onActionGetStartedClick() }
                isVisible = item == items.last()
            }

            with(binding.actionSkipTextView) {
                setOnClickListener { onActionSkipClick() }
                isVisible = item != items.last()
            }

            with(binding.actionPrevButton) {
                setOnClickListener { onActionPrevClick() }
                isVisible = item != items.first() && item != items.last()
            }

            with(binding.actionNextButton) {
                setOnClickListener { onActionNextClick() }
                isVisible = item != items.last()
            }
        }
    }
}