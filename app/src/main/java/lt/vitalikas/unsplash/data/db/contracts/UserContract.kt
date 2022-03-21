package lt.vitalikas.unsplash.data.db.contracts

object UserContract {

    const val TABLE_NAME = "feed_users"

    object Columns {
        const val USER_PROFILE_IMAGE_ID = "user_profile_image_id"
        const val USER_LINK_ID = "user_link_id"

        const val ID = "id"
        const val USERNAME = "username"
        const val NAME = "name"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val PORTFOLIO_URL = "portfolio_url"
        const val BIO = "bio"
        const val LOCATION = "location"
        const val TOTAL_LIKES = "total_likes"
        const val TOTAL_PHOTOS = "total_photos"
        const val TOTAL_COLLECTIONS = "total_collections"
        const val INSTAGRAM_USERNAME = "instagram_username"
        const val TWITTER_USERNAME = "twitter_username"

        const val LINKS = "links"
    }
}