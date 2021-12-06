package lt.vitalikas.unsplash.ui.start_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
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
        bindData()
        bindViewModel()
        start()
    }

    private fun bindData() {
        Glide.with(this)
            .load(R.drawable.start)
            .into(startImage)

        startTitle.text = getString(R.string.start_text)
    }

    private fun bindViewModel() {
        startViewModel.step.observe(viewLifecycleOwner) { millisLeft ->
            loadingText.text = (millisLeft / 1000 + 1).toString()
        }

        startViewModel.sharedPrefsStatus.observe(viewLifecycleOwner) { isFirstBoot ->
            navigateToNextScreen(isFirstBoot)
        }
    }

    private fun start() {
        startViewModel.timer?.start()
    }

    private fun navigateToNextScreen(isFirstBoot: Boolean) {
        if (isFirstBoot) {
            startViewModel.updateSharedPrefsStatus(false)
            findNavController().navigate(
                StartFragmentDirections.actionStartFragmentToOnboardingFragment()
            )
        } else {
            findNavController().navigate(
                StartFragmentDirections.actionStartFragmentToAuthFragment()
            )
        }
    }
}