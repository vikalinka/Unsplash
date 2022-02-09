package lt.vitalikas.unsplash.ui.splash_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentSplashBinding
import lt.vitalikas.unsplash.ui.onboarding_screen.OnboardingStatus

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val startImage get() = binding.ivStartImage
    private val startTitle get() = binding.tvStartTitle
    private val loadingText get() = binding.tvLoadingText
    private val progress get() = binding.progressBar

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
    }

    private fun bindData() {
        startTitle.text = getString(R.string.start_text)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    splashViewModel.timerStateFlow
                        .collect { step ->
                            when (step) {
                                0, 1, 2 -> {
                                    progress.progress = step * 33
                                    loadingText.text = getString(R.string.progress, step * 33)
                                    loadImageByStep(step + 1)
                                }
                                3 -> {
                                    progress.progress = step * 33
                                    loadingText.text = getString(R.string.progress, step * 33)
                                    val onboardingStatus = splashViewModel.onboardingStatus
                                    navigateOnStatus(onboardingStatus)
                                }
                            }
                        }
                }
            }
        }
    }

    private fun loadImageByStep(step: Int) {
        val imageUri = "@drawable/start$step"
        val imageRes = resources.getIdentifier(imageUri, "drawable", context?.packageName)
        Glide.with(this)
            .load(imageRes)
            .into(startImage)
    }

    private fun navigateOnStatus(status: OnboardingStatus) {
        when (status) {
            OnboardingStatus.NotFinished -> findNavController().navigate(
                SplashFragmentDirections.actionStartFragmentToOnboardingFragment()
            )
            OnboardingStatus.Finished -> findNavController().navigate(
                SplashFragmentDirections.actionStartFragmentToAuthFragment()
            )
        }
    }
}