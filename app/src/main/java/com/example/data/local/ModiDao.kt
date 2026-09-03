package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ModiDao {

  @Query("SELECT * FROM assessment_profile WHERE id = 1 LIMIT 1")
  fun getAssessmentProfile(): Flow<AssessmentProfileEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveAssessmentProfile(profile: AssessmentProfileEntity)

  @Query("UPDATE assessment_profile SET assignedTrack = :trackCode WHERE id = 1")
  suspend fun updateCareTrack(trackCode: String)

  @Query("UPDATE assessment_profile SET isCompleted = :completed WHERE id = 1")
  suspend fun updateAssessmentStatus(completed: Boolean)

  // Routines
  @Query("SELECT * FROM routine_items WHERE dateStr = :dateStr ORDER BY priorityOrder ASC")
  fun getRoutinesForDate(dateStr: String): Flow<List<RoutineItemEntity>>

  @Query("SELECT * FROM routine_items WHERE isCompleted = 1")
  fun getAllCompletedRoutines(): Flow<List<RoutineItemEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRoutines(items: List<RoutineItemEntity>)

  @Query("UPDATE routine_items SET isCompleted = :isCompleted, completedTimestamp = :timestamp WHERE id = :id")
  suspend fun updateRoutineCompletion(id: Int, isCompleted: Boolean, timestamp: Long?)

  @Query("SELECT * FROM routine_items")
  suspend fun getAllRoutinesSync(): List<RoutineItemEntity>

  @Query("SELECT * FROM routine_items WHERE dateStr = :dateStr ORDER BY priorityOrder ASC")
  suspend fun getRoutinesListForDateSync(dateStr: String): List<RoutineItemEntity>

  @Query("DELETE FROM routine_items WHERE id IN (:ids)")
  suspend fun deleteRoutinesByIds(ids: List<Int>)

  @Query("DELETE FROM routine_items WHERE dateStr = :dateStr")
  suspend fun deleteRoutinesForDate(dateStr: String)

  // Photo logs
  @Query("SELECT * FROM hair_photo_logs ORDER BY timestamp DESC")
  fun getAllPhotoLogs(): Flow<List<HairPhotoLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPhotoLog(log: HairPhotoLogEntity)

  @Query("DELETE FROM hair_photo_logs WHERE id = :id")
  suspend fun deletePhotoLog(id: Int)
}
