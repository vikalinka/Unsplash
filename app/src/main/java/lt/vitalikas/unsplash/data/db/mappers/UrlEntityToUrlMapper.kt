package lt.vitalikas.unsplash.data.db.mappers

import lt.vitalikas.unsplash.data.db.entities.UrlEntity
import lt.vitalikas.unsplash.domain.models.base.Url

class UrlEntityToUrlMapper : Mapper<UrlEntity, Url> {
    override fun map(from: UrlEntity): Url =
        Url(
            raw = from.raw,
            full = from.full,
            regular = from.regular,
            small = from.small,
            thumb = from.thumb
        )
}