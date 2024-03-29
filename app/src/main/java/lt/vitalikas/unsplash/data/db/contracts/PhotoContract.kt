package lt.vitalikas.unsplash.data.db.contracts

object PhotoContract {

    const val TABLE_NAME = "photos"

    object Columns {
        const val USER_ID = "user_id"
        const val URL_ID = "url_id"
        const val LINK_ID = "link_id"

        const val ID = "id"
        const val CREATED_AT = "created_at"
        const val UPDATED_AT = "updated_at"
        const val WIDTH = "width"
        const val HEIGHT = "height"
        const val COLOR = "color"
        const val BLUR_HASH = "blur_hash"
        const val LIKES = "likes"
        const val LIKED_BY_USER = "liked_by_user"
        const val DESCRIPTION = "description"
        const val LAST_UPDATED_AT = "last_updated_at"
    }
}