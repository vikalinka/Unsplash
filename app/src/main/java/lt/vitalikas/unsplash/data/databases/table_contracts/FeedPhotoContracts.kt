package lt.vitalikas.unsplash.data.databases.table_contracts

object FeedPhotoContracts {

    const val TABLE_NAME = "feed_photo"

    object Columns {
        const val ID = "id"
        const val CREATED_AT = "created_at"
        const val UPDATED_AT = "updated_at"
        const val WIDTH = "width"
        const val HEIGHT = "height"
        const val COLOR = "color"
        const val BLUR_HASH = "blur_hash"
        const val LIKES = "likes"
        const val LIKED_BY_USER = "liked_by_user"
        const val DESCRIPTION = "description"

        const val USER = "user"
        const val CURRENT_USER_FEED_COLLECTIONS = "current_user_collections"
        const val URLS = "urls"
        const val LINKS = "links"
    }
}