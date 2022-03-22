package lt.vitalikas.unsplash.data.db.contracts

object RemoteKeyContract {

    const val TABLE_NAME = "remote_keys"

    object Columns {
        const val PHOTO_ID = "id"

        const val PREV_KEY = "prev_key"
        const val NEXT_KEY = "next_key"
    }
}