package lt.vitalikas.unsplash.data.db.mappers

import lt.vitalikas.unsplash.data.db.entities.LinkEntity
import lt.vitalikas.unsplash.domain.models.photo.Photo

class LinkToLinkEntity : Mapper<Photo, LinkEntity> {
    override fun mapPojoToEntity(from: Photo): LinkEntity =
        LinkEntity(
            id = from.user.id,
            self = from.link.self,
            html = from.link.html,
            download = from.link.download,
            downloadLocation = from.link.downloadLocation
        )
}