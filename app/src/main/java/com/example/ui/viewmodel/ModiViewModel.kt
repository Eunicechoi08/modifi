package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AssessmentProfileEntity
import com.example.data.local.CareTrack
import com.example.data.local.HairPhotoLogEntity
import com.example.data.local.ModiDatabase
import com.example.data.local.ModiRepository
import com.example.data.local.RoutineItemEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Survey in-progress state
data class QuestionnaireState(
  val currentStep: Int = 1, // 1: Danger Red Flags, 2: Life Stage & Nutrition, 3: Pattern & Duration
  val dangerPatch: Boolean = false,
  val dangerPainErythema: Boolean = false,
  val dangerScarring: Boolean = false,
  val dangerOtherBodyHair: Boolean = false,
  val isPostpartum: Boolean = false,
  val isLactating: Boolean = false,
  val isIronDeficient: Boolean = false,
  val durationCategory: String = "1YEAR_MORE", // "UNDER_6M", "6M_TO_1Y", "1YEAR_MORE"
  val sheddingPerDay: String = "50_100",
  val primaryConcernArea: String = "가르마 선",
  val pastTriedProducts: String = "탈모 샴푸, 비오틴",
  val userName: String = "김지은",
  val userAge: Int = 36
)

class ModiViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: ModiRepository

  init {
    val db = ModiDatabase.getDatabase(application)
    repository = ModiRepository(db.modiDao())
  }

  val todayDateString: String = repository.getTodayDateString()

  val profile: StateFlow<AssessmentProfileEntity?> = repository.assessmentProfile
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val todayRoutines: StateFlow<List<RoutineItemEntity>> = repository.getRoutinesForDate(todayDateString)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val completedRoutines: StateFlow<List<RoutineItemEntity>> = repository.completedRoutines
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val photoLogs: StateFlow<List<HairPhotoLogEntity>> = repository.allPhotoLogs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _surveyState = MutableStateFlow(QuestionnaireState())
  val surveyState: StateFlow<QuestionnaireState> = _surveyState.asStateFlow()

  // Seed sample data on first run if empty
  init {
    viewModelScope.launch {
      repository.cleanupDuplicateRoutines()
      // Ensure default routines exist
      profile.collect { p ->
        if (p != null && p.isCompleted) {
          repository.ensureDefaultRoutinesForTrack(p.assignedTrack, todayDateString)
        }
      }
    }
  }

  fun updateSurveyState(update: (QuestionnaireState) -> QuestionnaireState) {
    _surveyState.value = update(_surveyState.value)
  }

  fun resetSurvey() {
    _surveyState.value = QuestionnaireState()
  }

  fun determineTrack(state: QuestionnaireState): CareTrack {
    // 1. Red Flags -> Clinic Track C
    if (state.dangerPatch || state.dangerPainErythema || state.dangerScarring || state.dangerOtherBodyHair) {
      return CareTrack.TRACK_C
    }
    // 2. Postpartum & Lactating / Under 1 year or nutritional -> Track A
    if (state.isPostpartum || state.isLactating || (state.isIronDeficient && state.durationCategory == "UNDER_6M")) {
      return CareTrack.TRACK_A
    }
    // 3. Chronic Widening Part Line -> Track B
    return CareTrack.TRACK_B
  }

  fun completeAssessment() {
    viewModelScope.launch {
      val state = _surveyState.value
      val assignedTrack = determineTrack(state)
      val entity = AssessmentProfileEntity(
        id = 1,
        userName = state.userName.ifBlank { "김지은" },
        userAge = state.userAge,
        isCompleted = true,
        assignedTrack = assignedTrack.code,
        dangerPatch = state.dangerPatch,
        dangerPainErythema = state.dangerPainErythema,
        dangerScarring = state.dangerScarring,
        dangerOtherBodyHair = state.dangerOtherBodyHair,
        isPostpartum = state.isPostpartum,
        isLactating = state.isLactating,
        isIronDeficient = state.isIronDeficient,
        durationCategory = state.durationCategory,
        sheddingPerDay = state.sheddingPerDay,
        primaryConcernArea = state.primaryConcernArea,
        pastTriedProducts = state.pastTriedProducts,
        assessmentTimestamp = System.currentTimeMillis()
      )
      repository.saveAssessment(entity)

      // Seed initial sample comparison photo log if empty
      seedInitialPhotoLogsIfEmpty(assignedTrack)
    }
  }

  fun switchTrack(track: CareTrack) {
    viewModelScope.launch {
      repository.updateTrack(track.code)
    }
  }

  fun toggleRoutine(routine: RoutineItemEntity) {
    viewModelScope.launch {
      repository.setRoutineCompleted(routine.id, !routine.isCompleted)
    }
  }

  fun addPhotoLog(
    targetArea: String,
    sheddingRating: String,
    densityRating: Int,
    note: String,
    photoUri: String? = null
  ) {
    viewModelScope.launch {
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
      val log = HairPhotoLogEntity(
        dateStr = sdf.format(Date()),
        targetArea = targetArea,
        sheddingRating = sheddingRating,
        densityRating = densityRating,
        note = note,
        photoUri = photoUri
      )
      repository.addPhotoLog(log)
    }
  }

  fun deletePhotoLog(id: Int) {
    viewModelScope.launch {
      repository.deletePhotoLog(id)
    }
  }

  private suspend fun seedInitialPhotoLogsIfEmpty(track: CareTrack) {
    // We add 2 sample baseline milestone records so user immediately sees ghost comparison
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    val now = System.currentTimeMillis()
    val dayMillis = 86400000L

    val log1 = HairPhotoLogEntity(
      dateStr = sdf.format(Date(now - dayMillis * 28)),
      timestamp = now - dayMillis * 28,
      targetArea = "가르마 선",
      sheddingRating = "일시증가(쉐딩기)",
      densityRating = 2,
      note = "관리 1주차 기준 사진 등록. 가르마 선 중심으로 연모화 비침 확인.",
      dayCountInTrack = 7
    )

    val log2 = HairPhotoLogEntity(
      dateStr = sdf.format(Date(now - dayMillis * 7)),
      timestamp = now - dayMillis * 7,
      targetArea = "가르마 선",
      sheddingRating = "안정됨",
      densityRating = 3,
      note = "외용제 도포 4주차. 쉐딩기 불안을 넘기고 잔디 모발 관찰 시작.",
      dayCountInTrack = 28
    )

    repository.addPhotoLog(log1)
    repository.addPhotoLog(log2)
  }
}
