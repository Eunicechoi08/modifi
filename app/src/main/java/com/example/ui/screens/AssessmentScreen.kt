package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ModiAlert
import com.example.ui.theme.ModiAlertLight
import com.example.ui.theme.ModiBorder
import com.example.ui.theme.ModiCharcoal
import com.example.ui.theme.ModiForest
import com.example.ui.theme.ModiIvory
import com.example.ui.theme.ModiRose
import com.example.ui.theme.ModiRoseLight
import com.example.ui.theme.ModiSage
import com.example.ui.theme.ModiSageLight
import com.example.ui.theme.ModiTextSecondary
import com.example.ui.theme.ModiWarning
import com.example.ui.theme.ModiWarningLight
import com.example.ui.viewmodel.ModiViewModel

@Composable
fun AssessmentScreen(
  viewModel: ModiViewModel,
  onNavigateBack: () -> Unit,
  onCompleteAssessment: () -> Unit
) {
  val surveyState by viewModel.surveyState.collectAsState()
  val scrollState = rememberScrollState()

  Surface(
    modifier = Modifier
      .fillMaxSize()
      .background(ModiIvory),
    color = ModiIvory
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      // Top Navigation Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        IconButton(
          onClick = {
            if (surveyState.currentStep > 1) {
              viewModel.updateSurveyState { it.copy(currentStep = it.currentStep - 1) }
            } else {
              onNavigateBack()
            }
          },
          modifier = Modifier.testTag("survey_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "이전 단계",
            tint = ModiForest
          )
        }

        Text(
          text = "모디파이 60초 맞춤 자가문진",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = ModiForest
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ModiSageLight)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "${surveyState.currentStep} / 3 단계",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Progress Bar
      LinearProgressIndicator(
        progress = { surveyState.currentStep / 3f },
        modifier = Modifier
          .fillMaxWidth()
          .height(6.dp)
          .clip(CircleShape),
        color = ModiForest,
        trackColor = ModiBorder
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Step Contents Scrollable Area
      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(scrollState)
      ) {
        when (surveyState.currentStep) {
          1 -> Step1DangerSigns(viewModel, surveyState)
          2 -> Step2LifeStageAndNutrition(viewModel, surveyState)
          3 -> Step3SymptomsAndProfile(viewModel, surveyState)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Bottom Button
      Button(
        onClick = {
          if (surveyState.currentStep < 3) {
            viewModel.updateSurveyState { it.copy(currentStep = it.currentStep + 1) }
          } else {
            viewModel.completeAssessment()
            onCompleteAssessment()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("survey_next_button"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = ModiForest,
          contentColor = ModiIvory
        )
      ) {
        Text(
          text = if (surveyState.currentStep < 3) "다음 단계로 (진행률 ${(surveyState.currentStep * 33.3f).toInt()}%)" else "나를 알아주는 맞춤 경로 확인하기",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

// -----------------------------------------------------------------------------------------
// STEP 1: 위험 신호 스크리닝 (Safe Triage)
// -----------------------------------------------------------------------------------------
@Composable
private fun Step1DangerSigns(
  viewModel: ModiViewModel,
  state: com.example.ui.viewmodel.QuestionnaireState
) {
  Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    // Section Header
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ModiRoseLight)
        .border(1.dp, ModiRose.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        .padding(16.dp)
    ) {
      Row(verticalAlignment = Alignment.Top) {
        Icon(
          imageVector = Icons.Default.Warning,
          contentDescription = null,
          tint = ModiRose,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "1단계 : 안전 선별 (Safe Triage)",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "홈케어 시작 전, 즉각적인 전문의 진료가 필요한 위험 신호가 있는지 먼저 확인합니다. 해당되는 항목을 모두 선택해 주세요.",
            fontSize = 12.sp,
            color = ModiCharcoal,
            lineHeight = 17.sp
          )
        }
      }
    }

    // Question 1-1
    SurveyCheckboxCard(
      title = "동전 모양의 둥근 탈모반(원형탈모)이 있나요?",
      description = "두피 특정 부위가 뚜렷한 경계를 가지고 동그랗게 비어있는 양상 (자가면역 질환 가능성)",
      isChecked = state.dangerPatch,
      onCheckedChange = { checked ->
        viewModel.updateSurveyState { it.copy(dangerPatch = checked) }
      }
    )

    // Question 1-2
    SurveyCheckboxCard(
      title = "두피 통증, 심한 붉은 발진, 진물이 동반되나요?",
      description = "두피를 만질 때 욱신거리는 통증이나 심한 화끈거림, 고름 뾰루지 (급성 염증성 질환)",
      isChecked = state.dangerPainErythema,
      onCheckedChange = { checked ->
        viewModel.updateSurveyState { it.copy(dangerPainErythema = checked) }
      }
    )

    // Question 1-3
    SurveyCheckboxCard(
      title = "모공이 닫혀 흉터처럼 매끈해진 부위가 있나요?",
      description = "모발이 빠진 자리에 모공 구멍이 보이지 않고 피부 표면처럼 닫힘 (반흔성 탈모 의심 - 골든타임 필수)",
      isChecked = state.dangerScarring,
      onCheckedChange = { checked ->
        viewModel.updateSurveyState { it.copy(dangerScarring = checked) }
      }
    )

    // Question 1-4
    SurveyCheckboxCard(
      title = "눈썹, 속눈썹 등 다른 신체 체모도 함께 빠지나요?",
      description = "두피 외 다른 부위의 털도 동반 탈락되는 전신성 탈모 의심 신호",
      isChecked = state.dangerOtherBodyHair,
      onCheckedChange = { checked ->
        viewModel.updateSurveyState { it.copy(dangerOtherBodyHair = checked) }
      }
    )

    // Clear confirmation if none selected
    val noneSelected = !state.dangerPatch && !state.dangerPainErythema && !state.dangerScarring && !state.dangerOtherBodyHair
    if (noneSelected) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(ModiSageLight)
          .padding(12.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = ModiForest,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "해당되는 위험 신호가 없습니다. (홈케어 적합성 통과)",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = ModiForest
          )
        }
      }
    } else {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(ModiAlertLight)
          .padding(12.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = ModiAlert,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "위험 신호가 감지되었습니다. 전문의 진료 연계 리포트가 함께 준비됩니다.",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ModiAlert
          )
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------------------
// STEP 2: 생애주기 & 영양 요인 (Life Stage & Nutrition)
// -----------------------------------------------------------------------------------------
@Composable
private fun Step2LifeStageAndNutrition(
  viewModel: ModiViewModel,
  state: com.example.ui.viewmodel.QuestionnaireState
) {
  Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ModiSageLight)
        .border(1.dp, ModiSage.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        .padding(16.dp)
    ) {
      Row(verticalAlignment = Alignment.Top) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = null,
          tint = ModiForest,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "2단계 : 생애주기 & 영양 상태",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "3040 여성 탈모의 대표적 원인인 출산, 호르몬, 저장철(페리틴) 결핍 여부를 확인합니다.",
            fontSize = 12.sp,
            color = ModiCharcoal,
            lineHeight = 17.sp
          )
        }
      }
    }

    SurveyCheckboxCard(
      title = "출산 후 1년 미만인가요?",
      description = "출산 2~4개월 차에 일어나는 대량 탈락은 에스트로겐 급감에 의한 정상 휴지기 탈모입니다. (약물 치료 비권장, 자연 회복)",
      isChecked = state.isPostpartum,
      onCheckedChange = { checked ->
        viewModel.updateSurveyState { it.copy(isPostpartum = checked) }
      }
    )

    SurveyCheckboxCard(
      title = "현재 모유 수유 중이신가요?",
      description = "수유 중에는 태아/영아 안전을 위해 미녹시딜 등 약물 흡수가 금기되며, 비약물적 보존 관리가 원칙입니다.",
      isChecked = state.isLactating,
      onCheckedChange = { checked ->
        viewModel.updateSurveyState { it.copy(isLactating = checked) }
      }
    )

    SurveyCheckboxCard(
      title = "무리한 다이어트, 빈혈, 극심한 만성 피로가 있나요?",
      description = "체내 저장철(페리틴) 수치가 70ng/mL 이하로 고갈되면 모낭 세포 분열이 멈춰 털이 빠질 수 있습니다.",
      isChecked = state.isIronDeficient,
      onCheckedChange = { checked ->
        viewModel.updateSurveyState { it.copy(isIronDeficient = checked) }
      }
    )

    // Helpful note
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(Color.White)
        .border(1.dp, ModiBorder, RoundedCornerShape(12.dp))
        .padding(14.dp)
    ) {
      Text(
        text = "💡 출산 및 저장철분 결핍에 의한 탈모는 '모낭이 파괴된 영구 탈모'가 아니라 '일시적 가역성 상태'이므로, 원인만 교정하면 자연스럽게 다시 자라납니다.",
        fontSize = 11.5.sp,
        color = ModiForest,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium
      )
    }
  }
}

// -----------------------------------------------------------------------------------------
// STEP 3: 탈모 지속 기간, 양상 및 기본 정보
// -----------------------------------------------------------------------------------------
@Composable
private fun Step3SymptomsAndProfile(
  viewModel: ModiViewModel,
  state: com.example.ui.viewmodel.QuestionnaireState
) {
  Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(ModiSageLight)
        .border(1.dp, ModiSage.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        .padding(16.dp)
    ) {
      Column {
        Text(
          text = "3단계 : 탈모 지속 기간 및 양상",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = ModiForest
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "가르마 선 모발의 가늘어짐 지속 기간과 하루 빠짐 정도를 선택해 주세요.",
          fontSize = 12.sp,
          color = ModiCharcoal
        )
      }
    }

    // Name & Age input
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      OutlinedTextField(
        value = state.userName,
        onValueChange = { name -> viewModel.updateSurveyState { it.copy(userName = name) } },
        label = { Text("이름 (호칭)") },
        modifier = Modifier
          .weight(1.2f)
          .testTag("user_name_input"),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = ModiForest,
          unfocusedBorderColor = ModiBorder
        )
      )

      OutlinedTextField(
        value = state.userAge.toString(),
        onValueChange = { ageStr ->
          val age = ageStr.filter { it.isDigit() }.toIntOrNull() ?: 36
          viewModel.updateSurveyState { it.copy(userAge = age) }
        },
        label = { Text("연령 (세)") },
        modifier = Modifier
          .weight(0.8f)
          .testTag("user_age_input"),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = ModiForest,
          unfocusedBorderColor = ModiBorder
        )
      )
    }

    // Duration Radio Group
    Text(
      text = "가르마 선 비침이나 모발 가늘어짐이 지속된 기간은?",
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      color = ModiForest
    )

    SurveyRadioOption(
      title = "6개월 미만 (최근 급격히 시작됨)",
      subtitle = "계절 변화, 스트레스, 일시적 영양 부족 가능성",
      isSelected = state.durationCategory == "UNDER_6M",
      onClick = { viewModel.updateSurveyState { it.copy(durationCategory = "UNDER_6M") } }
    )

    SurveyRadioOption(
      title = "6개월 ~ 1년 (서서히 진행 중)",
      subtitle = "가르마 선이 넓어지며 묶었을 때 숱이 줄어듦",
      isSelected = state.durationCategory == "6M_TO_1Y",
      onClick = { viewModel.updateSurveyState { it.copy(durationCategory = "6M_TO_1Y") } }
    )

    SurveyRadioOption(
      title = "1년 이상 지속된 만성 진행",
      subtitle = "두피가 훤히 비치고 잔머리가 굵게 자라지 못함 (여성형 탈모 완주 트랙 대상)",
      isSelected = state.durationCategory == "1YEAR_MORE",
      onClick = { viewModel.updateSurveyState { it.copy(durationCategory = "1YEAR_MORE") } }
    )

    Spacer(modifier = Modifier.height(6.dp))

    // Daily Shedding Feeling
    Text(
      text = "하루 머리 빠짐 체감량은 어느 정도인가요?",
      fontSize = 13.sp,
      fontWeight = FontWeight.Bold,
      color = ModiForest
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      listOf(
        "< 50개" to "정상 범위",
        "50~100개" to "주의 단계",
        "100개 이상" to "과다 탈락"
      ).forEach { (amt, desc) ->
        val selected = state.sheddingPerDay == amt
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) ModiForest else Color.White)
            .border(
              width = if (selected) 2.dp else 1.dp,
              color = if (selected) ModiForest else ModiBorder,
              shape = RoundedCornerShape(12.dp)
            )
            .clickable { viewModel.updateSurveyState { it.copy(sheddingPerDay = amt) } }
            .padding(vertical = 12.dp, horizontal = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = amt,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = if (selected) ModiIvory else ModiCharcoal
            )
            Text(
              text = desc,
              fontSize = 10.sp,
              color = if (selected) ModiSageLight else ModiTextSecondary
            )
          }
        }
      }
    }
  }
}

// -----------------------------------------------------------------------------------------
// Common UI helper cards
// -----------------------------------------------------------------------------------------
@Composable
private fun SurveyCheckboxCard(
  title: String,
  description: String,
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(if (isChecked) ModiSageLight.copy(alpha = 0.6f) else Color.White)
      .border(
        width = if (isChecked) 1.8.dp else 1.dp,
        color = if (isChecked) ModiForest else ModiBorder,
        shape = RoundedCornerShape(14.dp)
      )
      .clickable { onCheckedChange(!isChecked) }
      .padding(14.dp)
  ) {
    Row(verticalAlignment = Alignment.Top) {
      Box(
        modifier = Modifier
          .size(24.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(if (isChecked) ModiForest else Color.White)
          .border(
            width = 1.5.dp,
            color = if (isChecked) ModiForest else ModiBorder,
            shape = RoundedCornerShape(6.dp)
          ),
        contentAlignment = Alignment.Center
      ) {
        if (isChecked) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
        }
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = ModiCharcoal
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = description,
          fontSize = 11.5.sp,
          color = ModiTextSecondary,
          lineHeight = 16.sp
        )
      }
    }
  }
}

@Composable
private fun SurveyRadioOption(
  title: String,
  subtitle: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(if (isSelected) ModiSageLight.copy(alpha = 0.5f) else Color.White)
      .border(
        width = if (isSelected) 1.8.dp else 1.dp,
        color = if (isSelected) ModiForest else ModiBorder,
        shape = RoundedCornerShape(14.dp)
      )
      .clickable { onClick() }
      .padding(14.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(20.dp)
          .clip(CircleShape)
          .border(
            width = if (isSelected) 6.dp else 1.5.dp,
            color = if (isSelected) ModiForest else ModiBorder,
            shape = CircleShape
          )
      )
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = title,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = ModiCharcoal
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = ModiTextSecondary
        )
      }
    }
  }
}
