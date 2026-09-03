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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.ModiSageMedium
import com.example.ui.theme.ModiTextSecondary

@Composable
fun WelcomeScreen(
  onStartAssessment: () -> Unit,
  onContinueToHome: (() -> Unit)? = null
) {
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
      // Top confidential / brand tag
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(ModiSage)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "MODIFI CARE SERVICE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest,
            letterSpacing = 1.sp
          )
        }
        Text(
          text = "3040 맞춤 안심 케어",
          fontSize = 11.sp,
          color = ModiTextSecondary
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // -------------------------------------------------------------
      // Prominent Safe Self-Check Card (안전 자가점검 크게 띄움)
      // -------------------------------------------------------------
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(
            Brush.verticalGradient(
              colors = listOf(ModiForest, ModiForestDark)
            )
          )
          .border(1.5.dp, ModiSageMedium.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
          .padding(24.dp)
          .testTag("prominent_safe_triage_card")
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Top Safety Badge
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(ModiRoseLight)
              .padding(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = ModiRose,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "필수 사전 확인 • SAFE TRIAGE",
              fontSize = 11.5.sp,
              fontWeight = FontWeight.Black,
              color = ModiRose,
              letterSpacing = 0.5.sp
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Big Main Headline
          Text(
            text = "시작 전 필수,\n모발·두피 안전 자가점검",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ModiIvory,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "“모(毛)를 다르게 정의하고 바꾸다”\n맞춤 루틴 시작 전 위험 신호 4가지를 60초 만에 스크리닝합니다",
            fontSize = 13.sp,
            color = ModiSageLight,
            textAlign = TextAlign.Center,
            lineHeight = 19.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Key Screening Checkpoints Summary
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color.White.copy(alpha = 0.12f))
              .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ModiSageLight,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "동전 모양 원형탈모반 / 흉터성 모공 소실 확인",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = ModiIvory
              )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ModiSageLight,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "두피 화농성 염증·진물 및 급격한 탈락 통증 감별",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = ModiIvory
              )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = ModiSageLight,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "출산 100일 전후 휴지기 탈락 & 수유기 안전성 평가",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = ModiIvory
              )
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Big Primary Action Button
          Button(
            onClick = onStartAssessment,
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .testTag("start_assessment_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = ModiIvory,
              contentColor = ModiForest
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = ModiForest,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "지금 안전 자가점검 시작하기 (60초)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Secondary Brand & Philosophy Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(18.dp))
          .background(Color.White)
          .border(1.dp, ModiBorder, RoundedCornerShape(18.dp))
          .padding(18.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(ModiSageLight),
            contentAlignment = Alignment.Center
          ) {
            ModiLogoSymbol(
              size = 40.dp,
              strokeColor = ModiForest,
              strokeWidth = 2.8f
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "MODIFI",
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = ModiForest,
                fontFamily = FontFamily.Serif
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "毛 + Modify",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ModiSageMedium
              )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "나를 알아야 관리가 달라지니까 — 두피 루틴을 바꾸는 3040 맞춤 케어",
              fontSize = 11.5.sp,
              color = ModiCharcoal,
              lineHeight = 16.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 3 Core Pillars Section
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        PillarItem(
          icon = Icons.Default.Security,
          title = "1. 안전 자가점검 (Safe Triage)",
          description = "원형탈모·염증 등 위험 신호와 출산·수유 상태를 60초 만에 선제 스크리닝",
          badgeColor = ModiSageLight,
          iconColor = ModiForest
        )
        PillarItem(
          icon = Icons.Default.Spa,
          title = "2. 3가지 맞춤 경로 (A/B/C 트랙)",
          description = "출산·영양 안심, 가르마 집중 완주, 전문의 진료 연계 중 내 원인에 딱 맞는 경로 배정",
          badgeColor = ModiSageLight,
          iconColor = ModiSage
        )
        PillarItem(
          icon = Icons.Default.Favorite,
          title = "3. 12개월 안심 완주 지원",
          description = "초기 2~8주 쉐딩 공포 방어 코칭과 투명 겹침 가르마 사진으로 미세 변화 추적",
          badgeColor = ModiRoseLight,
          iconColor = ModiRose
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Action Buttons (Optional Continue to Home)
      if (onContinueToHome != null) {
        OutlinedButton(
          onClick = onContinueToHome,
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("continue_to_home_button"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = ModiForest
          ),
          border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = Brush.horizontalGradient(listOf(ModiSage, ModiForest))
          )
        ) {
          Text(
            text = "기존 진단 결과로 홈 바로가기",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Medical Disclaimer Footnote
      Text(
        text = "* 본 서비스는 의료법에 따른 의사의 진단을 대신하지 않으며, 사용자가 자신의 상태를 올바르게 인지하고 안전하게 관리 및 진료를 준비할 수 있도록 돕는 건강 자가점검 서비스입니다.",
        fontSize = 10.sp,
        color = ModiTextSecondary,
        textAlign = TextAlign.Center,
        lineHeight = 14.sp,
        modifier = Modifier.padding(bottom = 12.dp)
      )
    }
  }
}

@Composable
private fun PillarItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  description: String,
  badgeColor: Color,
  iconColor: Color
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(Color.White)
      .border(1.dp, ModiBorder, RoundedCornerShape(14.dp))
      .padding(14.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(42.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(badgeColor),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = iconColor,
        modifier = Modifier.size(22.dp)
      )
    }
    Spacer(modifier = Modifier.width(14.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = ModiCharcoal
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = description,
        fontSize = 11.sp,
        color = ModiTextSecondary,
        lineHeight = 15.sp
      )
    }
  }
}
