package lt.vitalikas.unsplash.ui.profile_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentProfileBinding
import lt.vitalikas.unsplash.domain.models.Profile

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val avatar get() = binding.ivAvatar
    private val name get() = binding.tvName
    private val username get() = binding.tvUsername
    private val location get() = binding.tvLocation
    private val email get() = binding.tvEmail
    private val downloadCount get() = binding.tvDownloadCount
    private val photoCount get() = binding.tvPhotoCount
    private val likeCount get() = binding.tvLikeCount
    private val collectionCount get() = binding.tvCollectionCount
    private val photos get() = binding.rvPhotos

    private val profileViewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfile()
        bindViewModel()
    }

    private fun getProfile() {
        profileViewModel.getProfile()
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

    private fun bindViewModel() {
        profileViewModel.profile.observe(viewLifecycleOwner) { profile ->
            bindProfileData(profile)
        }
    }
}