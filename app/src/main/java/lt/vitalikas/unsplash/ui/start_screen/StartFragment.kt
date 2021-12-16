package lt.vitalikas.unsplash.ui.start_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentStartBinding

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    private val binding by viewBinding(FragmentStartBinding::bind)
    private val startImage get() = binding.ivStartImage
    private val startTitle get() = binding.tvStartTitle
    private val loadingText get() = binding.tvLoadingText

    private val startViewModel by viewModels<StartViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        lifecycleScope.launch {
            delay(500)
            bindData()
            start()
        }
    }

    private fun bindData() {
        startTitle.text = getString(R.string.start_text)
    }

    private fun bindViewModel() {
        startViewModel.step.observe(viewLifecycleOwner) { millisLeft ->
            loadingText.text = (millisLeft / (1000 - 100)).toString()
            loadImage(((millisLeft / (1000 - 100))).toInt())
        }

        startViewModel.onboardingNotFinishedStatus.observe(viewLifecycleOwner) { onboardingNotFinished ->
            navigateToNextScreen(onboardingNotFinished)
        }
    }

    private fun start() {
        startViewModel.timer?.start()
    }

    private fun navigateToNextScreen(onboardingNotFinished: Boolean) {
        if (onboardingNotFinished) {
            findNavController().navigate(
                StartFragmentDirections.actionStartFragmentToOnboardingFragment()
            )
        } else {
            findNavController().navigate(
                StartFragmentDirections.actionStartFragmentToAuthFragment()
            )
        }
    }

    private fun loadImage(num: Int) {
        val imageUri = "@drawable/start$num"
        val imageRes = resources.getIdentifier(imageUri, "drawable", context?.packageName)
        Glide.with(this)
            .load(imageRes)
            .into(startImage)
    }
}