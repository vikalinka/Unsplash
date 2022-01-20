package lt.vitalikas.unsplash.data.databases.table_contracts

object UserProfileImagesContract {

    const val TABLE_NAME = "user_profile_images"

    object Columns {
        const val ID = "id"
        const val SMALL = "small"
        const val MEDIUM = "medium"
        const val LARGE = "large"
    }
}