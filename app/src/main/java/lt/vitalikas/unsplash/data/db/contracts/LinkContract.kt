package lt.vitalikas.unsplash.data.db.contracts

object LinkContract {

    const val TABLE_NAME = "links"

    object Columns {
        const val ID = "id"
        const val SELF = "self"
        const val HTML = "html"
        const val DOWNLOAD = "download"
        const val DOWNLOAD_LOCATION = "download_location"
    }
}