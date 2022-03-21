package lt.vitalikas.unsplash.data.db.contracts

object UserProfileImageContract {

    const val TABLE_NAME = "user_profile_images"

    object Columns {
        const val ID = "id"
        const val SMALL = "small"
        const val MEDIUM = "medium"
        const val LARGE = "large"
    }
}