package lt.vitalikas.unsplash.ui.profile_screen

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentProfileBinding
import lt.vitalikas.unsplash.domain.models.Profile
import lt.vitalikas.unsplash.ui.onboarding_screen.OnboardingTransformer
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val avatar get() = binding.ivAvatar
    private val name get() = binding.tvName
    private val usernameIcon get() = binding.ivUsername
    private val username get() = binding.tvUsername
    private val locationIcon get() = binding.ivLocation
    private val location get() = binding.tvLocation
    private val emailIcon get() = binding.ivEmail
    private val email get() = binding.tvEmail
    private val downloadsIcon get() = binding.ivDownloads
    private val downloadCount get() = binding.tvDownloadCount
    private val photoCount get() = binding.tvPhotoCount
    private val likeCount get() = binding.tvLikeCount
    private val collectionCount get() = binding.tvCollectionCount
    private val photosPager get() = binding.vpPhotos
    private val loadingProgressBar get() = binding.pbLoading

    private val profileViewModel by viewModels<ProfileViewModel>()

    private val photoAdapter
        get() = requireNotNull(photosPager.adapter as ProfileAdapter) {
            error("Photo adapter not initialized")
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPhotoPager()
        initPagerPositionListener()
        getProfileData()
        bindViewModel()
        initBackButtonNav()
    }

    private fun getProfileData() {
        profileViewModel.getProfileData()
    }

    private fun bindProfileData(profile: Profile) {
        Glide.with(this)
            .load(profile.image.large)
            .placeholder(R.drawable.placeholder_profile)
            .error(R.drawable.placeholder_profile)
            .into(avatar)

        name.text = profile.name
        username.text = profile.username
        location.text = profile.location
        email.text = profile.email
        downloadCount.text = profile.downloads.toString()
        photoCount.text = getString(R.string.photos, profile.totalPhotos)
        likeCount.text = getString(R.string.likes, profile.totalLikes)
        collectionCount.text = getString(R.string.collections, profile.totalCollections)

        photoAdapter.items = profile.photos
        Timber.d("page pos viewmodel = ${profileViewModel.position}")

        photosPager.currentItem = profileViewModel.position ?: 2
    }

    private fun toggleViewsVisibility(isVisible: Boolean) {
        listOf(
            avatar,
            name,
            usernameIcon,
            username,
            locationIcon,
            location,
            emailIcon,
            email,
            downloadsIcon,
            downloadCount,
            photoCount,
            likeCount,
            collectionCount,
            photosPager
        ).forEach { view ->
            view.isVisible = !isVisible
        }
        loadingProgressBar.isVisible = isVisible
    }

    private fun initPhotoPager() {
        with(photosPager) {
            adapter = ProfileAdapter()

            offscreenPageLimit = 1

            setPageTransformer(OnboardingTransformer())
        }
    }

    private fun initPagerPositionListener() {
        photosPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Timber.d("page listener pos = $position")
                profileViewModel.position = position
                Timber.d("page listener pos = ${profileViewModel.position}")
            }
        })
    }

    private fun bindViewModel() {
        profileViewModel.dataState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileDataState.Loading -> {
                    toggleViewsVisibility(state.isLoading)
                }
                is ProfileDataState.Success -> {
                    bindProfileData(state.profile)
                }
                is ProfileDataState.Error -> {

                }
            }
        }
    }

    private fun initBackButtonNav() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                        ?.let { nav ->
                            nav.selectedItemId = R.id.home
                        } ?: error("View not found")
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        profileViewModel.cancelScopeChildrenJobs()
    }
}