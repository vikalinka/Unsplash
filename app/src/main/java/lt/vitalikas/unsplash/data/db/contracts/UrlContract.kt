package lt.vitalikas.unsplash.data.db.contracts

object UrlContract {

    const val TABLE_NAME = "urls"

    object Columns {
        const val ID = "id"
        const val RAW = "raw"
        const val FULL = "full"
        const val REGULAR = "regular"
        const val SMALL = "small"
        const val THUMB = "thumb"
    }
}