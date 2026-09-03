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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CareTrack
import com.example.data.local.RoutineItemEntity
import com.example.ui.components.ModiLogoSymbol
import com.example.ui.theme.ModiAlert
import com.example.ui.theme.ModiAlertLight
import com.example.ui.theme.ModiBorder
import com.example.ui.theme.ModiCharcoal
import com.example.ui.theme.ModiForest
import com.example.ui.theme.ModiForestDark
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
fun HomeScreen(
  viewModel: ModiViewModel,
  onNavigateToPhotoLog: () -> Unit,
  onNavigateToDoctorReport: () -> Unit,
  onNavigateToMyPage: () -> Unit
) {
  val profile by viewModel.profile.collectAsState()
  val routines by viewModel.todayRoutines.collectAsState()
  val displayRoutines = remember(routines) {
    routines.distinctBy { it.title }
  }
  val completedRoutines by viewModel.completedRoutines.collectAsState()
  val activeTrack = CareTrack.fromCode(profile?.assignedTrack ?: "B")
  val userName = profile?.userName ?: "김지은"

  val completedTodayCount = displayRoutines.count { it.isCompleted }
  val totalTodayCount = displayRoutines.size
  val progressFraction = if (totalTodayCount > 0) completedTodayCount.toFloat() / totalTodayCount else 0f

  var showSheddingDialog by remember { mutableStateOf(false) }
  var selectedTipArticle by remember { mutableStateOf<Pair<String, String>?>(null) }

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
        .verticalScroll(scrollState)
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      // Top Bar: Brand, User Greeting, & Track Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { onNavigateToMyPage() }
        ) {
          ModiLogoSymbol(
            size = 32.dp,
            strokeColor = ModiForest,
            strokeWidth = 2.4f
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "MODIFI",
              fontSize = 15.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 2.sp,
              color = ModiForest,
              fontFamily = FontFamily.Serif
            )
            Text(
              text = "모(毛)를 다르게 정의하고 바꾸다",
              fontSize = 10.sp,
              color = ModiTextSecondary
            )
          }
        }

        // Active Track Chip
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
              when (activeTrack) {
                CareTrack.TRACK_A -> ModiSageLight
                CareTrack.TRACK_B -> ModiForest
                CareTrack.TRACK_C -> ModiRoseLight
              }
            )
            .clickable { onNavigateToMyPage() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("active_track_chip")
        ) {
          Text(
            text = activeTrack.badgeLabel,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (activeTrack == CareTrack.TRACK_B) ModiIvory else ModiForest
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Buddy Warm Greeting Card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ModiForest)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "오늘의 버디 메시지 🌿",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = ModiSageLight
            )
            Text(
              text = "Day 14",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = ModiIvory.copy(alpha = 0.8f)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "${userName} 님, 오늘도 나를 알아가는 하루 시작해볼까요?",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = ModiIvory,
            lineHeight = 24.sp
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = when (activeTrack) {
              CareTrack.TRACK_A -> "산후 100일경 탈락은 아기와 나를 지킨 호르몬의 정상적인 회복 여정이에요. 조급해하지 말고 영양부터 든든하게 채워요."
              CareTrack.TRACK_B -> "모낭 세포가 새로운 힘을 얻는 중입니다. 오늘 저녁 1분 외용제 도포와 두피 쿨링을 잊지 마세요."
              CareTrack.TRACK_C -> "위험 신호가 감지되었을 땐 자가 처치보다 전문의 상담이 가장 빠른 길입니다. 진료 리포트를 챙겨보세요."
            },
            fontSize = 12.5.sp,
            color = ModiSageLight,
            lineHeight = 18.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // -------------------------------------------------------------
      // Shedding Death-Valley Coaching Banner (Crucial for 3040 Women!)
      // -------------------------------------------------------------
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(ModiRoseLight)
          .border(1.2.dp, ModiRose.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
          .clickable { showSheddingDialog = true }
          .padding(16.dp)
          .testTag("shedding_banner")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ModiRose),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "초기 2~8주 '쉐딩 현상' 안심 코칭",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ModiForest
              )
              Text(
                text = "약 바르고 머리가 더 빠져서 불안하신가요? ➔ 원리 확인하기",
                fontSize = 11.sp,
                color = ModiCharcoal,
                lineHeight = 15.sp
              )
            }
          }
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "자세히 보기",
            tint = ModiRose
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // -------------------------------------------------------------
      // Daily Routine Checklist Section
      // -------------------------------------------------------------
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "오늘의 버디 실천 루틴",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
          Text(
            text = "${viewModel.todayDateString} • ${completedTodayCount}/${totalTodayCount} 완료",
            fontSize = 11.5.sp,
            color = ModiTextSecondary
          )
        }

        // Percentage Circle
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ModiSageLight)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = "${(progressFraction * 100).toInt()}% 달성",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Linear progress bar
      LinearProgressIndicator(
        progress = { progressFraction },
        modifier = Modifier
          .fillMaxWidth()
          .height(7.dp)
          .clip(CircleShape),
        color = ModiForest,
        trackColor = ModiBorder
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Routine Items List
      Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        if (displayRoutines.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color.White)
              .padding(20.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "오늘의 루틴을 준비하고 있습니다...",
              fontSize = 13.sp,
              color = ModiTextSecondary
            )
          }
        } else {
          displayRoutines.forEach { item ->
            RoutineItemRow(
              item = item,
              onToggle = { viewModel.toggleRoutine(item) }
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // -------------------------------------------------------------
      // 12-Month Milestone Journey Card
      // -------------------------------------------------------------
      Text(
        text = "12개월 안심 완주 마일스톤",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = ModiForest
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "모발 주기는 12개월입니다. 조급한 포기 대신 타임라인을 따라가세요.",
        fontSize = 11.5.sp,
        color = ModiTextSecondary
      )

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        MilestoneChip(step = "1M", title = "적응기", desc = "도포 습관화", isCurrent = true, modifier = Modifier.weight(1f))
        MilestoneChip(step = "3M", title = "쉐딩 돌파", desc = "잔디모 발현", isCurrent = false, modifier = Modifier.weight(1f))
        MilestoneChip(step = "6M", title = "굵기 개선", desc = "연모화 완화", isCurrent = false, modifier = Modifier.weight(1f))
        MilestoneChip(step = "12M", title = "안심 회복", desc = "가르마 정돈", isCurrent = false, modifier = Modifier.weight(1f))
      }

      Spacer(modifier = Modifier.height(24.dp))

      // -------------------------------------------------------------
      // Quick Action Navigation Cards (Ghost Camera & Doctor Report)
      // -------------------------------------------------------------
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Ghost Overlay Camera Card
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, ModiBorder, RoundedCornerShape(16.dp))
            .clickable { onNavigateToPhotoLog() }
            .padding(16.dp)
            .testTag("nav_photo_log_button")
        ) {
          Column {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ModiSageLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = null,
                tint = ModiForest,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "고스트 겹침 기록",
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Bold,
              color = ModiCharcoal
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "동일 각도 가르마 비교",
              fontSize = 11.sp,
              color = ModiTextSecondary
            )
          }
        }

        // Doctor Report Card
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, ModiBorder, RoundedCornerShape(16.dp))
            .clickable { onNavigateToDoctorReport() }
            .padding(16.dp)
            .testTag("nav_doctor_report_button")
        ) {
          Column {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ModiRoseLight),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = null,
                tint = ModiRose,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "진료 준비 리포트",
              fontSize = 13.5.sp,
              fontWeight = FontWeight.Bold,
              color = ModiCharcoal
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "피부과 의사용 요약본",
              fontSize = 11.sp,
              color = ModiTextSecondary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // -------------------------------------------------------------
      // 3040 Evidence-Based Knowledge Cards
      // -------------------------------------------------------------
      Text(
        text = "나를 알아주는 두피 지식",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = ModiForest
      )

      Spacer(modifier = Modifier.height(12.dp))

      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        KnowledgeCard(
          title = "Q. 비싼 탈모 샴푸를 쓰는데 왜 효과가 없을까요?",
          summary = "샴푸는 두피에 머무르는 시간이 1~3분에 불과한 세정제입니다. 모낭 속 깊은 곳의 호르몬이나 모근 세포 분열을 실질적으로 바꾸기 어렵습니다.",
          fullText = "탈모 기능성 샴푸는 두피의 과도한 피지와 각질을 씻어내 청결한 환경을 만드는 보조적 수단입니다. 하지만 여성 탈모의 근본 원인인 호르몬 변동이나 혈액 속 저장철 결핍, 모낭 위축은 샴푸의 짧은 접촉 시간 동안 모낭 진피층까지 도달할 수 없습니다. 따라서 고가의 샴푸에만 의존하기보다, 의학적으로 입증된 1차 외용제(미녹시딜) 도포나 영양 교정이 핵심입니다.",
          onClick = { selectedTipArticle = "탈모 샴푸의 진실" to it }
        )

        KnowledgeCard(
          title = "Q. 여성 탈모에서 '페리틴(저장철)'이 중요한 이유?",
          summary = "단순 혈색소(헤모글로빈)가 정상이어도 체내 저장철(페리틴)이 70ng/mL 미만이면 모발 성장기가 단축됩니다.",
          fullText = "우리 몸은 철분이 부족해지면 생명 유지에 필수적인 심장과 뇌로 철분을 우선 공급하고, 상대적으로 덜 중요한 모발 세포로 가는 철분을 차단합니다. 탈모로 내원하는 여성의 약 70%가 페리틴 저하 상태입니다. 식단에 붉은 살코기, 깻잎, 계란 노른자를 챙기고 비타민C와 함께 섭취하면 모발 굵기 회복에 큰 도움이 됩니다.",
          onClick = { selectedTipArticle = "페리틴(저장철)의 비밀" to it }
        )

        KnowledgeCard(
          title = "Q. 가르마를 주기적으로 1cm씩 바꿔야 하는 이유는?",
          summary = "동일한 가르마 선만 유지하면 자외선 노출이 집중되고, 모근의 지속적 견인으로 해당 부위 연모화가 가속됩니다.",
          fullText = "가르마를 타는 위치는 자외선(UV)과 외부 건조 자극을 온전히 받습니다. 매달 1cm씩 왼쪽이나 오른쪽으로 지그재그 변경해주면, 특정 두피 부위의 열 스트레스와 견인력을 분산시켜 가르마 선이 휑해지는 것을 효과적으로 예방할 수 있습니다.",
          onClick = { selectedTipArticle = "가르마 위치 변경의 원리" to it }
        )
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }

  // -----------------------------------------------------------------
  // Shedding Coaching Dialog
  // -----------------------------------------------------------------
  if (showSheddingDialog) {
    AlertDialog(
      onDismissRequest = { showSheddingDialog = false },
      confirmButton = {
        Button(
          onClick = { showSheddingDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = ModiForest)
        ) {
          Text("안심하고 완주할게요", color = ModiIvory)
        }
      },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = ModiRose,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "초기 2~8주 쉐딩(Shedding) 안심 가이드",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
        }
      },
      text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
          Text(
            text = "“약 바르고 머리가 더 빠져서 무서워요!”",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = ModiRose
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "여성 탈모 관리자의 86.3%가 바로 이 시기에 부작용으로 오해하고 관리를 중단합니다.\n\n" +
              "하지만 이것은 약효가 정상적으로 작동하고 있다는 가장 확실한 신호입니다.\n\n" +
              "💡 쉐딩의 의학적 원리:\n" +
              "외용제를 바르면 모낭이 활성화되면서, 이미 빠질 운명이었던 가늘고 힘없는 '휴지기 모발'을 밀어내고, 그 자리에서 새롭고 굵은 '성장기 모발'이 자라기 시작합니다.\n\n" +
              "낡은 잎이 떨어져야 싱싱한 새싹이 돋는 것과 같습니다. 보통 2~4주 차에 시작되어 6~8주 차에 멈추므로, 절대 중단하지 말고 3개월 차 잔디 모발을 기다려 주세요.",
            fontSize = 12.sp,
            color = ModiCharcoal,
            lineHeight = 18.sp
          )
        }
      },
      shape = RoundedCornerShape(16.dp),
      containerColor = Color.White
    )
  }

  // Knowledge Article Dialog
  if (selectedTipArticle != null) {
    AlertDialog(
      onDismissRequest = { selectedTipArticle = null },
      confirmButton = {
        TextButton(onClick = { selectedTipArticle = null }) {
          Text("확인", color = ModiForest, fontWeight = FontWeight.Bold)
        }
      },
      title = {
        Text(
          text = selectedTipArticle!!.first,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = ModiForest
        )
      },
      text = {
        Text(
          text = selectedTipArticle!!.second,
          fontSize = 13.sp,
          color = ModiCharcoal,
          lineHeight = 19.sp
        )
      },
      shape = RoundedCornerShape(16.dp),
      containerColor = Color.White
    )
  }
}

@Composable
private fun RoutineItemRow(
  item: RoutineItemEntity,
  onToggle: () -> Unit
) {
  val isDone = item.isCompleted

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(if (isDone) ModiSageLight.copy(alpha = 0.5f) else Color.White)
      .border(
        width = if (isDone) 1.5.dp else 1.dp,
        color = if (isDone) ModiSage else ModiBorder,
        shape = RoundedCornerShape(14.dp)
      )
      .clickable { onToggle() }
      .padding(14.dp)
      .testTag("routine_item_${item.id}")
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      Box(
        modifier = Modifier
          .size(24.dp)
          .clip(CircleShape)
          .background(if (isDone) ModiForest else Color.Transparent)
          .border(
            width = if (isDone) 0.dp else 1.5.dp,
            color = if (isDone) Color.Transparent else ModiBorder,
            shape = CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        if (isDone) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "완료",
            tint = ModiIvory,
            modifier = Modifier.size(15.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.title,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = if (isDone) ModiTextSecondary else ModiCharcoal,
          textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = item.description,
          fontSize = 11.sp,
          color = ModiTextSecondary,
          lineHeight = 15.sp
        )
      }
    }
  }
}

@Composable
private fun MilestoneChip(
  step: String,
  title: String,
  desc: String,
  isCurrent: Boolean,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(if (isCurrent) ModiForest else Color.White)
      .border(
        width = if (isCurrent) 1.5.dp else 1.dp,
        color = if (isCurrent) ModiForest else ModiBorder,
        shape = RoundedCornerShape(12.dp)
      )
      .padding(8.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = step,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = if (isCurrent) ModiRose else ModiForest
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = title,
        fontSize = 11.5.sp,
        fontWeight = FontWeight.Bold,
        color = if (isCurrent) ModiIvory else ModiCharcoal
      )
      Text(
        text = desc,
        fontSize = 9.sp,
        color = if (isCurrent) ModiSageLight else ModiTextSecondary
      )
    }
  }
}

@Composable
private fun KnowledgeCard(
  title: String,
  summary: String,
  fullText: String,
  onClick: (String) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(Color.White)
      .border(1.dp, ModiBorder, RoundedCornerShape(14.dp))
      .clickable { onClick(fullText) }
      .padding(14.dp)
  ) {
    Row(
      verticalAlignment = Alignment.Top,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = null,
            tint = ModiRose,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = title,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = summary,
          fontSize = 11.sp,
          color = ModiTextSecondary,
          lineHeight = 15.sp
        )
      }
      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = "보기",
        tint = ModiBorder,
        modifier = Modifier.padding(top = 2.dp)
      )
    }
  }
}
