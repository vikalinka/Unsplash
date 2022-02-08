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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentStartBinding

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_start) {

    private val binding by viewBinding(FragmentStartBinding::bind)
    private val startImage get() = binding.ivStartImage
    private val startTitle get() = binding.tvStartTitle
    private val loadingText get() = binding.tvLoadingText

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
    }

    private fun bindData() {
        startTitle.text = getString(R.string.start_text)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                delay(500)

                launch {
                    splashViewModel.timerFlow
                        .collect { step ->
                            if (step == 0) {
                                val onboardingStatus = splashViewModel.onboardingNotFinished
                                navigateToNextScreen(onboardingStatus)
                            } else {
                                loadingText.text = step.toString()
                                loadImageByStep(step)
                            }
                        }
                }
            }
        }
    }

    private fun navigateToNextScreen(onboardingNotFinished: Boolean) {
        if (onboardingNotFinished) {
            findNavController().navigate(
                SplashFragmentDirections.actionStartFragmentToOnboardingFragment()
            )
        } else {
            findNavController().navigate(
                SplashFragmentDirections.actionStartFragmentToAuthFragment()
            )
        }
    }

    private fun loadImageByStep(step: Int) {
        val imageUri = "@drawable/start$step"
        val imageRes = resources.getIdentifier(imageUri, "drawable", context?.packageName)
        Glide.with(this)
            .load(imageRes)
            .into(startImage)
    }
}