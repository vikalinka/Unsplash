package lt.vitalikas.unsplash.data.db.mappers

import lt.vitalikas.unsplash.data.db.entities.UserLinkEntity
import lt.vitalikas.unsplash.domain.models.user.User

class UserLinkToUserLinkEntity : Mapper<User, UserLinkEntity> {
    override fun mapPojoToEntity(from: User): UserLinkEntity =
        UserLinkEntity(
            id = from.id,
            self = from.link.self,
            html = from.link.html,
            photos = from.link.photos,
            likes = from.link.likes,
            portfolio = from.link.portfolio,
        )
}