package lt.vitalikas.unsplash.data.db.mappers

import lt.vitalikas.unsplash.data.db.entities.LinkEntity
import lt.vitalikas.unsplash.domain.models.photo.Link

class LinkEntityToLinkMapper : Mapper<LinkEntity, Link> {
    override fun map(from: LinkEntity): Link =
        Link(
            self = from.self,
            html = from.html,
            download = from.download,
            downloadLocation = from.downloadLocation
        )
}