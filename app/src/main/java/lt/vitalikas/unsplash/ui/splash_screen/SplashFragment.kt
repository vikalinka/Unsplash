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
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentSplashBinding

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val welcomeImage get() = binding.welcomeImageView
    private val welcomeText get() = binding.welcomeTextView
    private val progressText get() = binding.progressTextView
    private val progress get() = binding.loadingProgressBar

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
    }

    private fun bindData() {
        welcomeText.text = getString(R.string.welcome_text)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                splashViewModel.timerStateFlow
                    .collect { step ->
                        when (step) {
                            0, 1, 2 -> {
                                progress.progress = step * 33
                                progressText.text = getString(R.string.progress, step * 33)
                                loadImageByStep(step + 1)
                            }
                            3 -> {
                                progress.progress = step * 33
                                progressText.text = getString(R.string.progress, step * 33)
                                splashViewModel.onboardingsStatus
                                    .collect { status ->
                                        navigateOnStatus(status)
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
            .into(welcomeImage)
    }

    private fun navigateOnStatus(isFinishedStatus: Boolean) {
        if (isFinishedStatus) {
            findNavController().navigate(
                SplashFragmentDirections.actionStartFragmentToAuthFragment()
            )
        } else {
            findNavController().navigate(
                SplashFragmentDirections.actionStartFragmentToOnboardingFragment()
            )
        }
    }
}
