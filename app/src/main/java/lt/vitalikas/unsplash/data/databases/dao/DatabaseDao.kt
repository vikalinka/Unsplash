package lt.vitalikas.unsplash.data.databases.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import lt.vitalikas.unsplash.data.databases.entities.FeedPhotoEntity
import lt.vitalikas.unsplash.data.databases.entities.FeedUserEntity

@Database(
    entities = [
        FeedPhotoEntity::class,
        FeedUserEntity::class
    ],
    version = DatabaseDao.DB_VERSION
)

abstract class DatabaseDao : RoomDatabase() {

    abstract fun feedPhotosDao(): FeedPhotosDao
    abstract fun feedUserDao(): FeedUserDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "db"
    }
}