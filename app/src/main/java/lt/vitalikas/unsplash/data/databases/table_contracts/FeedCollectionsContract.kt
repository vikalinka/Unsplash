package lt.vitalikas.unsplash.data.databases.table_contracts

object FeedCollectionsContract {

    const val TABLE_NAME = "feed_collections"

    object Columns {
        const val FEED_PHOTO_ID = "feed_photo_id"

        const val ID = "id"
        const val TITLE = "title"
        const val PUBLISHED_AT = "published_at"
        const val LAST_COLLECTED_AT = "last_collected_at"
        const val UPDATED_AT = "updated_at"
        const val COVER_PHOTO = "cover_photo"
        const val USER = "user"
    }
}