package lt.vitalikas.unsplash.data.db

import android.content.Context
import androidx.room.Room
import lt.vitalikas.unsplash.data.db.dao.DatabaseDao

object Database {

    lateinit var instance: DatabaseDao
        private set

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context,
            DatabaseDao::class.java,
            DatabaseDao.DB_NAME
        )
            .build()
    }
}