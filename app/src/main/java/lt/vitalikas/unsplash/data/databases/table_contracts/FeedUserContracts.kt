package lt.vitalikas.unsplash.data.databases.table_contracts

object FeedUserContracts {

    const val TABLE_NAME = "feed_user"

    object Columns {
        const val FEED_PHOTO_ID = "feed_photo_id"

        const val ID = "id"
        const val USERNAME = "username"
        const val NAME = "name"
        const val PORTFOLIO_URL = "portfolio_url"
        const val BIO = "bio"
        const val LOCATION = "location"
        const val TOTAL_LIKES = "total_likes"
        const val TOTAL_PHOTOS = "total_photos"
        const val TOTAL_COLLECTIONS = "total_collections"
        const val INSTAGRAM_USERNAME = "instagram_username"
        const val TWITTER_USERNAME = "twitter_username"

        const val PROFILE_IMAGE = "profile_image"
        const val LINKS = "links"
    }
}