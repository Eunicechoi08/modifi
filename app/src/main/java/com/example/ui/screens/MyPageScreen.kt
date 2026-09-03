package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CareTrack
import com.example.ui.components.ModiLogoSymbol
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
fun MyPageScreen(
  viewModel: ModiViewModel,
  onNavigateBack: () -> Unit,
  onRetakeAssessment: () -> Unit
) {
  val profile by viewModel.profile.collectAsState()
  val activeTrack = CareTrack.fromCode(profile?.assignedTrack ?: "B")
  val userName = profile?.userName ?: "김지은"
  val userAge = profile?.userAge ?: 36

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
      // Top Bar
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onNavigateBack,
          modifier = Modifier.testTag("mypage_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "뒤로가기",
            tint = ModiForest
          )
        }

        Text(
          text = "내 모디파이 관리 & 설정",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = ModiForest
        )

        Spacer(modifier = Modifier.size(48.dp))
      }

      Spacer(modifier = Modifier.height(12.dp))

      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // User Profile Summary Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, ModiBorder, RoundedCornerShape(18.dp))
            .padding(18.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(ModiSageLight),
              contentAlignment = Alignment.Center
            ) {
              ModiLogoSymbol(
                size = 38.dp,
                strokeColor = ModiForest,
                strokeWidth = 2.4f
              )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${userName} 님 ($userAge 세)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ModiCharcoal
              )
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = "현재 관리: ${activeTrack.title}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = ModiForest
              )
            }
          }
        }

        // Re-Assessment Action Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ModiSageLight)
            .border(1.dp, ModiSage.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .clickable { onRetakeAssessment() }
            .padding(16.dp)
            .testTag("retake_assessment_card")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = ModiForest,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "자가문진 다시 진행하기",
                  fontSize = 13.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = ModiForest
                )
                Text(
                  text = "출산 1년 경과, 두피 상태 변화 시 언제든 재평가",
                  fontSize = 11.sp,
                  color = ModiTextSecondary
                )
              }
            }
            Icon(
              imageVector = Icons.Default.ChevronRight,
              contentDescription = null,
              tint = ModiForest
            )
          }
        }

        // Track Quick Switcher
        Text(
          text = "관리 경로 변경 (A/B/C)",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = ModiForest
        )

        CareTrack.entries.forEach { track ->
          val isSelected = activeTrack == track
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(if (isSelected) ModiForest else Color.White)
              .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) ModiForest else ModiBorder,
                shape = RoundedCornerShape(14.dp)
              )
              .clickable { viewModel.switchTrack(track) }
              .padding(14.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = track.title,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) ModiIvory else ModiCharcoal
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = track.summary,
                  fontSize = 11.sp,
                  color = if (isSelected) ModiSageLight else ModiTextSecondary
                )
              }
              if (isSelected) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ModiRose)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                  Text(
                    text = "적용 중",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                }
              }
            }
          }
        }

        // Brand Story & Meaning Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, ModiBorder, RoundedCornerShape(16.dp))
            .padding(18.dp)
        ) {
          Column {
            Text(
              text = "모디파이(MODIFI) 브랜드 이야기",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = ModiForest
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "■ 모(毛) + Modify :\n나의 모발과 두피 루틴을 건강하게 변화시킵니다.\n\n" +
                "■ 모(毛) + Define Differently :\n모발의 탈락 원인을 다르게 정의하고 접근합니다.\n\n" +
                "메인 슬로건 :\n“모(毛)를 다르게 정의하고 바꾸다, 3040 맞춤 탈모 솔루션 모디파이”\n\n" +
                "“나를 알아야 관리가 달라지니까 — 두피 루틴을 바꾸는 맞춤 케어, 모디파이(MODIFI)”",
              fontSize = 12.sp,
              color = ModiCharcoal,
              lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "* 의학적 근거: 유럽피부과학회(EDF) S3 임상 가이드라인 및 대한모발학회 원칙 준용",
              fontSize = 10.5.sp,
              color = ModiSage
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}
