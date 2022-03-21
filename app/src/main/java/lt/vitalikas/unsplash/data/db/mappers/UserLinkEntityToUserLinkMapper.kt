package lt.vitalikas.unsplash.data.db.mappers

import lt.vitalikas.unsplash.data.db.entities.UserLinkEntity
import lt.vitalikas.unsplash.domain.models.user.UserLink

class UserLinkEntityToUserLinkMapper : Mapper<UserLinkEntity, UserLink> {
    override fun map(from: UserLinkEntity): UserLink =
        UserLink(
            self = from.self,
            html = from.html,
            photos = from.photos,
            likes = from.likes,
            portfolio = from.portfolio
        )
}