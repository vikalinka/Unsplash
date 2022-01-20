package lt.vitalikas.unsplash.data.databases.table_contracts

object FeedPhotosContract {

    const val TABLE_NAME = "feed_photos"

    object Columns {
        const val USER_ID = "user_id"
        const val FEED_URL_ID = "feed_url_id"
        const val FEED_LINK_ID = "feed_link_id"

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
    }
}