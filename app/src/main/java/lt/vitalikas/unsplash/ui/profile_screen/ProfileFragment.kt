package lt.vitalikas.unsplash.ui.profile_screen

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentProfileBinding
import lt.vitalikas.unsplash.domain.models.Profile

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun initPhotoPager(photos: List<Profile.Photo>) {
        with(photosPager) {
            adapter = ProfilePhotoAdapter(photos)

            offscreenPageLimit = 1

            setPageTransformer(false, ProfilePhotoTransformer())
        }
    }

    private fun bindViewModel() {
        profileViewModel.dataState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileDataState.Loading -> {
                    toggleViewsVisibility(state.isLoading)
                }
                is ProfileDataState.Success -> {
                    initPhotoPager(state.profile.photos)
                    bindProfileData(state.profile)
                }
                is ProfileDataState.Error -> {

                }
                is ProfileDataState.Cancellation -> {

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