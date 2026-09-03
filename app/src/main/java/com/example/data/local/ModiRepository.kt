package com.example.data.local

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ModiRepository(private val dao: ModiDao) {

  val assessmentProfile: Flow<AssessmentProfileEntity?> = dao.getAssessmentProfile()
  val allPhotoLogs: Flow<List<HairPhotoLogEntity>> = dao.getAllPhotoLogs()
  val completedRoutines: Flow<List<RoutineItemEntity>> = dao.getAllCompletedRoutines()

  fun getTodayDateString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    return sdf.format(Date())
  }

  fun getRoutinesForDate(dateStr: String): Flow<List<RoutineItemEntity>> {
    return dao.getRoutinesForDate(dateStr)
  }

  suspend fun saveAssessment(profile: AssessmentProfileEntity) {
    dao.saveAssessmentProfile(profile)
    ensureDefaultRoutinesForTrack(profile.assignedTrack, getTodayDateString())
  }

  suspend fun updateTrack(trackCode: String) {
    dao.updateCareTrack(trackCode)
    ensureDefaultRoutinesForTrack(trackCode, getTodayDateString())
  }

  suspend fun setRoutineCompleted(id: Int, isCompleted: Boolean) {
    val timestamp = if (isCompleted) System.currentTimeMillis() else null
    dao.updateRoutineCompletion(id, isCompleted, timestamp)
  }

  suspend fun addPhotoLog(log: HairPhotoLogEntity) {
    dao.insertPhotoLog(log)
  }

  suspend fun deletePhotoLog(id: Int) {
    dao.deletePhotoLog(id)
  }

  suspend fun cleanupDuplicateRoutines() {
    val all = dao.getAllRoutinesSync()
    val toDeleteIds = mutableListOf<Int>()
    val grouped = all.groupBy { it.dateStr to it.title }
    for ((_, items) in grouped) {
      if (items.size > 1) {
        // Keep the completed one if any, otherwise keep the earliest ID
        val keep = items.firstOrNull { it.isCompleted } ?: items.first()
        for (item in items) {
          if (item.id != keep.id) {
            toDeleteIds.add(item.id)
          }
        }
      }
    }
    if (toDeleteIds.isNotEmpty()) {
      dao.deleteRoutinesByIds(toDeleteIds)
    }
  }

  suspend fun ensureDefaultRoutinesForTrack(trackCode: String, dateStr: String) {
    cleanupDuplicateRoutines()

    val existing = dao.getRoutinesListForDateSync(dateStr)
    if (existing.isNotEmpty()) {
      val existingTrack = existing.firstOrNull()?.trackCode
      if (existingTrack == trackCode && existing.size >= 4) {
        // All default routines already present without duplicates
        return
      } else if (existingTrack != null && existingTrack != trackCode) {
        // Track was switched (e.g. from B to C), clear and re-initialize for new track
        dao.deleteRoutinesForDate(dateStr)
      }
    }

    val items = when (trackCode) {
      CareTrack.TRACK_A.code -> listOf(
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "NUTRITION",
          title = "페리틴(저장철) & 헴철 식단 섭취",
          description = "모낭 분열의 원료가 되는 저장철과 비타민C 식단 챙기기",
          priorityOrder = 1
        ),
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "CARE",
          title = "수유부 맞춤 단백질 영양 보충",
          description = "모발 케라틴 합성을 위한 달걀, 두부, 저염 식단 유지",
          priorityOrder = 2
        ),
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "HABIT",
          title = "저자극 두피 마사지 & 열감 쿨링",
          description = "미온수로 부드럽게 세정하고 두피 열감 낮추기",
          priorityOrder = 3
        ),
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "MIND",
          title = "산후 회복 안심 마인드셋",
          description = "출산 후 100일경 탈락은 정상 호르몬 반응임을 기억하기",
          priorityOrder = 4
        )
      )
      CareTrack.TRACK_B.code -> listOf(
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "CARE",
          title = "가르마 선 1차 외용제 정량 도포",
          description = "두피를 완전히 말린 후 가르마 선 중심으로 골고루 도포",
          priorityOrder = 1
        ),
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "MIND",
          title = "쉐딩 데스밸리 안심 체크",
          description = "초기 2~8주차 일시적 빠짐은 건강한 모발 순환 반응입니다",
          priorityOrder = 2
        ),
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "HABIT",
          title = "가르마 방향 1cm 재정돈",
          description = "동일 부위 자외선 집중 노출과 모근 견인 분산하기",
          priorityOrder = 3
        ),
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "HABIT",
          title = "주간 고스트 겹침 사진 촬영",
          description = "동일 조도·투명도 가이드라인으로 미세 모발 변화 기록",
          priorityOrder = 4
        )
      )
      CareTrack.TRACK_C.code -> listOf(
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "CARE",
          title = "자극성 홈케어 샴푸·영양제 일시 정지",
          description = "손상 방지를 위해 임의 홈케어를 멈추고 전문의 진료 대기",
          priorityOrder = 1
        ),
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "HABIT",
          title = "모디파이 의사용 진료 요약 리포트 확인",
          description = "발병 시점, 통증 양상, 위험 신호를 리포트에 정리",
          priorityOrder = 2
        ),
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "HABIT",
          title = "근처 모발 전문 피부과 진료 예약",
          description = "골든타임 내 정확한 진단과 면역/처방 상담 받기",
          priorityOrder = 3
        ),
        RoutineItemEntity(
          dateStr = dateStr,
          trackCode = trackCode,
          category = "CARE",
          title = "미온수 저자극 두피 헹굼",
          description = "환부에 물리적 자극을 주지 않고 부드럽게 세정",
          priorityOrder = 4
        )
      )
      else -> emptyList()
    }

    val currentExisting = dao.getRoutinesListForDateSync(dateStr)
    val currentTitles = currentExisting.map { it.title }.toSet()
    val newItems = items.filter { it.title !in currentTitles }
    if (newItems.isNotEmpty()) {
      dao.insertRoutines(newItems)
    }
  }
}
