package lt.vitalikas.unsplash.data.databases.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import lt.vitalikas.unsplash.data.databases.entities.*
import lt.vitalikas.unsplash.domain.models.FeedPhoto
import lt.vitalikas.unsplash.domain.models.User

@Database(
    entities = [
        FeedPhotoEntity::class,
        UserEntity::class,
        UserProfileImageEntity::class,
        UserLinkEntity::class,
        RemoteKey::class
    ],
    version = DatabaseDao.DB_VERSION
)

abstract class DatabaseDao : RoomDatabase() {

    abstract fun feedPhotosDao(): FeedPhotosDao
    abstract fun feedUserDao(): FeedUserDao
    abstract fun feedUserProfileImageDao(): FeedUserProfileImageDao
    abstract fun feedUserLinkDao(): FeedUserLinkDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "db"
    }
}