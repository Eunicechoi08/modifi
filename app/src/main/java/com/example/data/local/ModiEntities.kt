package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assessment_profile")
data class AssessmentProfileEntity(
  @PrimaryKey val id: Int = 1,
  val userName: String = "김지은",
  val userAge: Int = 36,
  val isCompleted: Boolean = false,
  val assignedTrack: String = CareTrack.TRACK_B.code,
  val dangerPatch: Boolean = false,        // 동전 모양 원형탈모반
  val dangerPainErythema: Boolean = false, // 극심한 두피 통증/진물
  val dangerScarring: Boolean = false,     // 모공 소실(흉터) 의심
  val dangerOtherBodyHair: Boolean = false,// 눈썹/체모 동반 탈락
  val isPostpartum: Boolean = false,       // 출산 1년 미만
  val isLactating: Boolean = false,        // 모유 수유 중
  val isIronDeficient: Boolean = false,    // 저장철(페리틴) 결핍/빈혈/무리한 다이어트
  val durationCategory: String = "1YEAR_MORE", // "UNDER_6M", "6M_TO_1Y", "1YEAR_MORE"
  val sheddingPerDay: String = "50_100",   // "<50", "50_100", ">100"
  val primaryConcernArea: String = "가르마 선",
  val pastTriedProducts: String = "탈모 샴푸, 비오틴 영양제",
  val assessmentTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "routine_items")
data class RoutineItemEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val dateStr: String,                     // YYYY-MM-DD
  val trackCode: String,                   // A, B, C
  val category: String,                    // "CARE", "NUTRITION", "HABIT", "MIND"
  val title: String,
  val description: String,
  val isCompleted: Boolean = false,
  val completedTimestamp: Long? = null,
  val priorityOrder: Int = 0
)

@Entity(tableName = "hair_photo_logs")
data class HairPhotoLogEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val dateStr: String,                     // YYYY-MM-DD
  val timestamp: Long = System.currentTimeMillis(),
  val targetArea: String = "가르마 선",     // 가르마 선, 정수리, 앞머리/헤어라인
  val photoUri: String? = null,
  val sheddingRating: String = "보통",      // 안정됨, 보통, 쉐딩기 일시증가
  val densityRating: Int = 3,              // 1~5
  val note: String = "",
  val dayCountInTrack: Int = 1
)
