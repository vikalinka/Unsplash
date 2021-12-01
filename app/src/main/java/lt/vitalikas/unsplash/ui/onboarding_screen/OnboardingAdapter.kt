package lt.vitalikas.unsplash.ui.onboarding_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import lt.vitalikas.unsplash.databinding.ItemOnboardingBinding
import lt.vitalikas.unsplash.domain.models.OnboardingItem

class OnboardingAdapter(
    private val items: List<OnboardingItem>
) : RecyclerView.Adapter<OnboardingAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(
            ItemOnboardingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: Holder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class Holder(
        private val binding: ItemOnboardingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OnboardingItem) {
            Glide.with(itemView)
                .load(item.image)
                .into(binding.ivOnboardingImage)

            binding.tvOnboardingText.text = itemView.resources.getText(item.text)
        }
    }
}