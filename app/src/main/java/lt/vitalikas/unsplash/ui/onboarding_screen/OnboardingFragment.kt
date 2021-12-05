package lt.vitalikas.unsplash.ui.onboarding_screen

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentOnboardingBinding

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val binding by viewBinding(FragmentOnboardingBinding::bind)
    private val viewPager get() = binding.viewPager
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
                onboardingViewModel.screens,
                onBtnClick = {
                    findNavController().navigate(
                        OnboardingFragmentDirections.actionOnboardingFragmentToAuthFragment()
                    )
                },
                onActionSkipClick = {
                    findNavController().navigate(
                        OnboardingFragmentDirections.actionOnboardingFragmentToAuthFragment()
                    )
                },
                onActionNextClick = {
                    val screens = onboardingViewModel.screens
                    if (this.currentItem != screens.indexOf(screens.last())) {
                        this.currentItem += 1
                    }
                }
            )

            offscreenPageLimit = 1

            setPageTransformer { page, position ->
                when {
                    position < -1 || position > 1 -> {
                        page.alpha = 0f
                    }
                    position <= 0 -> {
                        page.alpha = 1 + position
                    }
                    position <= 1 -> {
                        page.alpha = 1 - position
                    }
                }
            }
        }

        dotsIndicator.setViewPager2(viewPager)
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