package lt.vitalikas.unsplash.data.databases.table_contracts

object FeedUrlsContract {

    const val TABLE_NAME = "feed_urls"

    object Columns {
        const val FEED_PHOTO_ID = "feed_photo_id"

        const val RAW = "raw"
        const val FULL = "full"
        const val REGULAR = "regular"
        const val SMALL = "small"
        const val THUMB = "thumb"
    }
}