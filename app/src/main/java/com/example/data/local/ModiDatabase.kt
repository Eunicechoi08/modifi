package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [
    AssessmentProfileEntity::class,
    RoutineItemEntity::class,
    HairPhotoLogEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class ModiDatabase : RoomDatabase() {
  abstract fun modiDao(): ModiDao

  companion object {
    @Volatile
    private var INSTANCE: ModiDatabase? = null

    fun getDatabase(context: Context): ModiDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          ModiDatabase::class.java,
          "modi_hair_care.db"
        ).build()
        INSTANCE = instance
        instance
      }
    }
  }
}
