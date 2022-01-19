package lt.vitalikas.unsplash.data.databases.table_contracts

object RemoteKeysContract {

    const val TABLE_NAME = "remote_keys"

    object Columns {
        const val FEED_PHOTO_ID = "id"

        const val PREV_KEY = "prev_key"
        const val NEXT_KEY = "next_key"
    }
}