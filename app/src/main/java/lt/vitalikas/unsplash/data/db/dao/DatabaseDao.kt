package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import lt.vitalikas.unsplash.data.db.entities.*

@Database(
    entities = [
        PhotoEntity::class,
        UserEntity::class,
        UserProfileImageEntity::class,
        UserLinkEntity::class,
        UrlEntity::class,
        LinkEntity::class,
        FeedCollectionEntity::class,
        RemoteKey::class
    ],
    version = DatabaseDao.DB_VERSION
)

abstract class DatabaseDao : RoomDatabase() {

    abstract fun photosDao(): PhotosDao
    abstract fun userDao(): UserDao
    abstract fun userProfileImageDao(): UserProfileImageDao
    abstract fun userLinkDao(): UserLinkDao
    abstract fun urlDao(): UrlDao
    abstract fun linkDao(): LinkDao
    abstract fun feedCollectionDao(): FeedCollectionDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "db"
    }
}