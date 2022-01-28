package lt.vitalikas.unsplash.data.db.contracts

object UserLinksContract {

    const val TABLE_NAME = "user_links"

    object Columns {
        const val ID = "id"
        const val SELF = "self"
        const val HTML = "html"
        const val PHOTOS = "photos"
        const val LIKES = "likes"
        const val PORTFOLIO = "portfolio"
    }
}