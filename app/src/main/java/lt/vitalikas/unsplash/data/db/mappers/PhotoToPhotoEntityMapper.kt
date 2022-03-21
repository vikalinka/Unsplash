package lt.vitalikas.unsplash.data.db.mappers

import android.icu.util.Calendar
import lt.vitalikas.unsplash.data.db.entities.PhotoEntity
import lt.vitalikas.unsplash.domain.models.photo.Photo

class PhotoToPhotoEntityMapper : Mapper<Photo, PhotoEntity> {
    override fun map(from: Photo): PhotoEntity =
        PhotoEntity(
            id = from.id,
            userId = from.user.id,
            feedUrlId = from.user.id,
            feedLinkId = from.user.id,
            createdAt = from.createdAt,
            updatedAt = from.updatedAt,
            width = from.width,
            height = from.height,
            color = from.color,
            blurHash = from.blurHash,
            likes = from.likes,
            likedByUser = from.likedByUser,
            description = from.description,
            lastUpdatedAt = Calendar.getInstance()
        )
}