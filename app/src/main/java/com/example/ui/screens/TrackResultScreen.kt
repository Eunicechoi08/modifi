package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CareTrack
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
import com.example.ui.viewmodel.ModiViewModel

@Composable
fun TrackResultScreen(
  viewModel: ModiViewModel,
  onProceedToHome: () -> Unit
) {
  val profile by viewModel.profile.collectAsState()
  val surveyState by viewModel.surveyState.collectAsState()
  val assignedTrack = if (profile != null) {
    CareTrack.fromCode(profile!!.assignedTrack)
  } else {
    viewModel.determineTrack(surveyState)
  }

  val userName = profile?.userName ?: surveyState.userName.ifBlank { "김지은" }
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
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(ModiSageLight)
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          Text(
            text = "모디파이 맞춤 원인 분석 결과",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "${userName} 님은\n[${assignedTrack.title}]입니다",
          fontSize = 22.sp,
          fontWeight = FontWeight.Black,
          color = ModiForest,
          textAlign = TextAlign.Center,
          lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "“나를 알아야, 관리가 달라지니까.”\n복합 원인을 분석하여 맞춤 경로를 배정했습니다.",
          fontSize = 13.sp,
          color = ModiTextSecondary,
          textAlign = TextAlign.Center,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Big Track Card
        val cardBg = when (assignedTrack) {
          CareTrack.TRACK_A -> ModiSageLight
          CareTrack.TRACK_B -> Color.White
          CareTrack.TRACK_C -> ModiRoseLight
        }

        val cardBorder = when (assignedTrack) {
          CareTrack.TRACK_A -> ModiSage
          CareTrack.TRACK_B -> ModiForest
          CareTrack.TRACK_C -> ModiRose
        }

        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cardBg)
            .border(2.dp, cardBorder, RoundedCornerShape(20.dp))
            .padding(20.dp)
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(
                    when (assignedTrack) {
                      CareTrack.TRACK_A -> ModiSage
                      CareTrack.TRACK_B -> ModiForest
                      CareTrack.TRACK_C -> ModiRose
                    }
                  )
                  .padding(horizontal = 10.dp, vertical = 5.dp)
              ) {
                Text(
                  text = assignedTrack.badgeLabel,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }

              Icon(
                imageVector = when (assignedTrack) {
                  CareTrack.TRACK_A -> Icons.Default.Spa
                  CareTrack.TRACK_B -> Icons.Default.CheckCircle
                  CareTrack.TRACK_C -> Icons.Default.MedicalServices
                },
                contentDescription = null,
                tint = cardBorder,
                modifier = Modifier.size(24.dp)
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = assignedTrack.summary,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = ModiCharcoal
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = assignedTrack.coreAdvice,
              fontSize = 13.sp,
              color = ModiCharcoal,
              lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.8f))
                .border(1.dp, ModiBorder, RoundedCornerShape(10.dp))
                .padding(12.dp)
            ) {
              Column {
                Text(
                  text = "🎯 이 경로의 대상 기준",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = ModiForest
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                  text = assignedTrack.targetDescription,
                  fontSize = 11.5.sp,
                  color = ModiTextSecondary,
                  lineHeight = 16.sp
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3 Key Execution Promises for this user
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          when (assignedTrack) {
            CareTrack.TRACK_A -> {
              TrackActionItem(number = "1", title = "산후 회복 타임라인 제공", description = "출산 100일경 대량 탈락은 자연스러운 정상 회복 반응입니다.")
              TrackActionItem(number = "2", title = "수유부 맞춤 영양 체크인", description = "저장철(페리틴)과 단백질 식단 루틴으로 모낭 회복 지원")
              TrackActionItem(number = "3", title = "12개월 안심 전환 평가", description = "1년 경과 시에도 미회복 시 안전하게 여성형 탈모 트랙 전환")
            }
            CareTrack.TRACK_B -> {
              TrackActionItem(number = "1", title = "쉐딩 데스밸리 방어 코칭", description = "2~8주차 일시 탈락 시 생리학적 알림 팝업으로 86% 조기 포기 방어")
              TrackActionItem(number = "2", title = "고스트 겹침 가르마 사진", description = "이전 가르마 사진과 투명도를 겹쳐 동일 각도 미세 잔디 모발 추적")
              TrackActionItem(number = "3", title = "12개월 도포 습관화 스트릭", description = "1일 1회 외용제 도포 체크와 잔디 머리 성장 마일스톤 지급")
            }
            CareTrack.TRACK_C -> {
              TrackActionItem(number = "1", title = "일반 홈케어 긴급 잠금", description = "모낭 영구 손상 방지를 위해 자극적인 샴푸/자가 처치 중단")
              TrackActionItem(number = "2", title = "피부과 의사용 진료 요약서", description = "증상 발현 시점, 탈락 양상, 통증 유무를 담은 전문 리포트 즉시 발행")
              TrackActionItem(number = "3", title = "골든타임 내 전문의 내원", description = "원형탈모/모공소실 치료가 가능한 피부과 전문의 상담 연결")
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Bottom Start Button
      Button(
        onClick = onProceedToHome,
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("start_modi_home_button"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = ModiForest,
          contentColor = ModiIvory
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "두피 루틴을 바꾸는 맞춤 케어 시작하기",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun TrackActionItem(
  number: String,
  title: String,
  description: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(Color.White)
      .border(1.dp, ModiBorder, RoundedCornerShape(12.dp))
      .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(26.dp)
        .clip(CircleShape)
        .background(ModiForest),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = number,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = ModiIvory
      )
    }
    Spacer(modifier = Modifier.width(12.dp))
    Column {
      Text(
        text = title,
        fontSize = 12.5.sp,
        fontWeight = FontWeight.Bold,
        color = ModiCharcoal
      )
      Spacer(modifier = Modifier.height(1.dp))
      Text(
        text = description,
        fontSize = 11.sp,
        color = ModiTextSecondary,
        lineHeight = 15.sp
      )
    }
  }
}
