package lt.vitalikas.unsplash.data.databases.table_contracts

object ProfileImageContracts {

    const val TABLE_NAME = "profile_image"

    object Columns {
        const val FEED_USER_ID = "feed_user_id"

        const val SMALL = "small"
        const val MEDIUM = "medium"
        const val LARGE = "large"
    }
}