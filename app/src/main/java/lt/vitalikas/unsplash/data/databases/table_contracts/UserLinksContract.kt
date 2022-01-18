package lt.vitalikas.unsplash.data.databases.table_contracts

object UserLinksContract {

    const val TABLE_NAME = "user_links"

    object Columns {
        const val FEED_USER_ID = "feed_user_id"

        const val SELF = "self"
        const val HTML = "html"
        const val PHOTOS = "photos"
        const val LIKES = "likes"
        const val PORTFOLIO = "portfolio"
    }
}