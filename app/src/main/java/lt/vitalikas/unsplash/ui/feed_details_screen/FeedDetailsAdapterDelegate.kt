package lt.vitalikas.unsplash.ui.feed_details_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.ItemFeedPhotoDetailsBinding
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails

class FeedDetailsAdapterDelegate(
    private val onLocationClick: (lat: Double, lng: Double) -> Unit,
    private val onDownloadClick: (url: String) -> Unit
) :
    AbsListItemAdapterDelegate<FeedPhotoDetails, FeedPhotoDetails, FeedDetailsAdapterDelegate.FeedPhotoDetailsViewHolder>() {

    class FeedPhotoDetailsViewHolder(
        private val binding: ItemFeedPhotoDetailsBinding,
        onLocationClick: (lat: Double, lng: Double) -> Unit,
        onDownloadClick: (url: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var lat: Double? = null
        private var lng: Double? = null
        private lateinit var downloadUrl: String

        init {
            binding.ivLocation.setOnClickListener {
                if (lat != null && lng != null) {
                    onLocationClick(lat!!, lng!!)
                }
            }
            binding.tvDownload.setOnClickListener {
                onDownloadClick(downloadUrl)
            }
        }

        fun bind(item: FeedPhotoDetails) {

            lat = item.location.position.latitude
            lng = item.location.position.longitude
            downloadUrl = item.urls.raw

            binding.tvLocation.text =
                itemView.context.getString(
                    R.string.location,
                    item.location.country ?: "N/A", item.location.city ?: "N/A"
                )

            binding.tvTag.text =
                itemView.context.getString(R.string.tag, item.tags.joinToString { tag ->
                    "#${tag.title}"
                })

            binding.tvMadeWith.text = itemView.context.getString(R.string.made_with, item.exif.make)
            binding.tvModel.text = itemView.context.getString(R.string.model, item.exif.model)
            binding.tvExposure.text =
                itemView.context.getString(R.string.exposure, item.exif.exposureTime)
            binding.tvAperture.text =
                itemView.context.getString(R.string.aperture, item.exif.aperture)
            binding.tvFocalLength.text =
                itemView.context.getString(R.string.focal_length, item.exif.focalLength.toString())
            binding.tvIso.text = itemView.context.getString(R.string.iso, item.exif.iso.toString())
            binding.tvAbout.text = itemView.context.getString(
                R.string.about,
                item.user.username,
                item.user.bio ?: "N/A"
            )
            binding.tvDownloadCount.text = item.downloads.toString()
        }
    }

    override fun isForViewType(
        item: FeedPhotoDetails,
        items: MutableList<FeedPhotoDetails>,
        position: Int
    ): Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup): FeedPhotoDetailsViewHolder =
        FeedPhotoDetailsViewHolder(
            binding = ItemFeedPhotoDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onLocationClick = onLocationClick,
            onDownloadClick = onDownloadClick
        )

    override fun onBindViewHolder(
        item: FeedPhotoDetails,
        holder: FeedPhotoDetailsViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)
}