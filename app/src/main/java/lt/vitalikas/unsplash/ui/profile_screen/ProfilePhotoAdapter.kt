package lt.vitalikas.unsplash.ui.profile_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.nightlynexus.viewstatepageradapter.ViewStatePagerAdapter
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.ItemPhotoBinding
import lt.vitalikas.unsplash.domain.models.Profile

class ProfilePhotoAdapter(
    private val photos: List<Profile.Photo>
) : ViewStatePagerAdapter() {

    override fun getCount(): Int = photos.size

    override fun createView(container: ViewGroup?, position: Int): View {

        val photo = photos[position]

        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(container?.context),
            container,
            false
        )

        val view = binding.root

        Glide.with(view)
            .load(photo.urls.regular)
            .placeholder(R.drawable.picture)
            .error(R.drawable.picture)
            .into(binding.ivPhoto)

        return view
    }
}