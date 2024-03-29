package lt.vitalikas.unsplash.ui.onboarding_screen

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentOnboardingBinding

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)
    private val viewPager get() = binding.onboardingViewPager
    private val dotsIndicator get() = binding.dotsIndicator

    private val onboardingViewModel by viewModels<OnboardingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnboardingScreens()
        initBackButtonNav()
    }

    private fun initOnboardingScreens() {
        with(viewPager) {
            adapter = OnboardingAdapter(
                items = onboardingViewModel.screens,
                onActionGetStartedClick = finishOnboarding(),
                onActionSkipClick = finishOnboarding(),
                onActionPrevClick = showPrevOnboardingScreen(),
                onActionNextClick = showNextOnboardingScreen()
            )

            offscreenPageLimit = 1

            setPageTransformer(OnboardingTransformer())
        }

        dotsIndicator.setViewPager2(viewPager)
    }

    private fun showPrevOnboardingScreen(): () -> Unit = {
        val screens = onboardingViewModel.screens
        if (viewPager.currentItem != screens.indexOf(screens.first())) {
            viewPager.currentItem -= 1
        }
    }

    private fun showNextOnboardingScreen(): () -> Unit = {
        val screens = onboardingViewModel.screens
        if (viewPager.currentItem != screens.indexOf(screens.last())) {
            viewPager.currentItem += 1
        }
    }

    private fun finishOnboarding(): () -> Unit = {
        lifecycleScope.launch {
            onboardingViewModel.finishOnboardings()
        }

        findNavController().navigate(
            OnboardingFragmentDirections.actionOnboardingFragmentToAuthFragment()
        )
    }

    private fun initBackButtonNav() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val screens = onboardingViewModel.screens
                    if (viewPager.currentItem == screens.indexOf(screens.first())) {
                        requireActivity().finish()
                    } else {
                        viewPager.currentItem -= 1
                    }
                }
            })
    }
}
