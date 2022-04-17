package lt.vitalikas.unsplash.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import lt.vitalikas.unsplash.data.db.dao.*
import lt.vitalikas.unsplash.data.db.entities.*

@Database(
    entities = [
        PhotoEntity::class,
        UserEntity::class,
        UserProfileImageEntity::class,
        UserLinkEntity::class,
        UrlEntity::class,
        LinkEntity::class,
        RemoteKey::class
    ],
    version = 1
)
abstract class Db : RoomDatabase() {

    abstract fun photosDao(): PhotosDao
    abstract fun userDao(): UserDao
    abstract fun userProfileImageDao(): UserProfileImageDao
    abstract fun userLinkDao(): UserLinkDao
    abstract fun urlDao(): UrlDao
    abstract fun linkDao(): LinkDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        private var INSTANCE: Db? = null
        private val LOCK = Any()
        private const val DB_NAME = "db"

        fun getInstance(application: Application): Db {
            INSTANCE?.let { db ->
                return db
            }
            synchronized(LOCK) {
                INSTANCE?.let { db ->
                    return db
                }
                val db = Room.databaseBuilder(
                    application,
                    Db::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return INSTANCE!!
            }
        }
    }
}
