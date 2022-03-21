package lt.vitalikas.unsplash.data.db.mappers

import lt.vitalikas.unsplash.data.db.entities.UrlEntity
import lt.vitalikas.unsplash.domain.models.photo.Photo
import lt.vitalikas.unsplash.domain.models.user.User

class UrlToUrlEntity : Mapper<Photo, UrlEntity> {
    override fun mapPojoToEntity(from: Photo): UrlEntity =
        UrlEntity(
            id = from.id,
            raw = from.url.raw,
            full = from.url.full,
            regular = from.url.regular,
            small = from.url.small,
            thumb = from.url.thumb
        )
}