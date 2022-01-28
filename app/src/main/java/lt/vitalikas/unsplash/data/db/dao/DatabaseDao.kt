package lt.vitalikas.unsplash.data.db.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import lt.vitalikas.unsplash.data.db.entities.*

@Database(
    entities = [
        FeedPhotoEntity::class,
        UserEntity::class,
        UserProfileImageEntity::class,
        UserLinkEntity::class,
        FeedUrlEntity::class,
        FeedLinkEntity::class,
        FeedCollectionEntity::class,
        RemoteKey::class
    ],
    version = DatabaseDao.DB_VERSION
)

abstract class DatabaseDao : RoomDatabase() {

    abstract fun feedPhotosDao(): FeedPhotosDao
    abstract fun feedUserDao(): FeedUserDao
    abstract fun feedUserProfileImageDao(): FeedUserProfileImageDao
    abstract fun feedUserLinkDao(): FeedUserLinkDao
    abstract fun feedUrlDao(): FeedUrlDao
    abstract fun feedLinkDao(): FeedLinkDao
    abstract fun feedCollectionDao(): FeedCollectionDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "db"
    }
}