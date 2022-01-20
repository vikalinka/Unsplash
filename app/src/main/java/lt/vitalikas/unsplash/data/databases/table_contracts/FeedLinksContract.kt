package lt.vitalikas.unsplash.data.databases.table_contracts

object FeedLinksContract {

    const val TABLE_NAME = "feed_links"

    object Columns {
        const val ID = "id"
        const val SELF = "self"
        const val HTML = "html"
        const val DOWNLOAD = "download"
        const val DOWNLOAD_LOCATION = "download_location"
    }
}