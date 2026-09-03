package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
fun DoctorReportScreen(
  viewModel: ModiViewModel,
  onNavigateBack: () -> Unit
) {
  val profile by viewModel.profile.collectAsState()
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  val userName = profile?.userName ?: "김지은"
  val userAge = profile?.userAge ?: 36
  val assignedTrack = CareTrack.fromCode(profile?.assignedTrack ?: "B")

  val reportPlainText = buildString {
    appendLine("[모디파이(MODIFI) 피부과 진료 준비 요약 리포트]")
    appendLine("■ 환자 기본 인적사항: $userName ($userAge 세, 여성)")
    appendLine("■ 배정 관리 경로: [${assignedTrack.title}]")
    appendLine("■ 주 호소 증상: ${profile?.primaryConcernArea ?: "가르마 선"} 부위 모발 가늘어짐 및 비침")
    appendLine("■ 발병 지속 기간: ${if (profile?.durationCategory == "1YEAR_MORE") "1년 이상 만성 진행" else "6개월~1년 이내"}")
    appendLine("■ 1일 탈락량 체감: ${profile?.sheddingPerDay ?: "50~100개"}")
    appendLine("■ 안전 선별(Red Flags):")
    appendLine("  - 원형 탈모반: ${if (profile?.dangerPatch == true) "의심/있음" else "없음"}")
    appendLine("  - 두피 통증/진물/발진: ${if (profile?.dangerPainErythema == true) "있음" else "없음"}")
    appendLine("  - 모공 소실(흉터성): ${if (profile?.dangerScarring == true) "의심" else "정상"}")
    appendLine("  - 타 체모 탈락: ${if (profile?.dangerOtherBodyHair == true) "있음" else "없음"}")
    appendLine("■ 생애주기 및 영양 상태:")
    appendLine("  - 출산 1년 미만: ${if (profile?.isPostpartum == true) "해당" else "해당 없음"}")
    appendLine("  - 수유 여부: ${if (profile?.isLactating == true) "수유 중" else "비수유"}")
    appendLine("  - 페리틴(저장철) 결핍/빈혈: ${if (profile?.isIronDeficient == true) "의심" else "미인지"}")
    appendLine("■ 기존 시도 이력: ${profile?.pastTriedProducts ?: "탈모 샴푸, 비오틴"}")
    appendLine("※ 본 리포트는 의료법에 따른 진단서가 아니며, 의료진의 정확하고 신속한 진료를 돕기 위한 사전 요약서입니다.")
  }

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
      // Top Navigation
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onNavigateBack,
          modifier = Modifier.testTag("doctor_report_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "뒤로가기",
            tint = ModiForest
          )
        }

        Text(
          text = "피부과 진료 준비 리포트",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = ModiForest
        )

        IconButton(
          onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("MODIFI 진료 요약서", reportPlainText)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "리포트 전체 내용이 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier.testTag("copy_report_button")
        ) {
          Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = "복사하기",
            tint = ModiForest
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Scrollable Report Body
      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(scrollState)
      ) {
        // Document Header Sheet
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.5.dp, ModiBorder, RoundedCornerShape(18.dp))
            .padding(20.dp)
        ) {
          Column {
            // Official Report Branding
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                ModiLogoSymbol(
                  size = 28.dp,
                  strokeColor = ModiForest,
                  strokeWidth = 2.2f
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = "MODIFI CLINIC REPORT",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = ModiForest
                  )
                  Text(
                    text = "의료진 상담 준비용 사전 요약서",
                    fontSize = 10.sp,
                    color = ModiTextSecondary
                  )
                }
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(ModiSageLight)
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "자가문진 완료",
                  fontSize = 10.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = ModiForest
                )
              }
            }

            HorizontalDivider(
              modifier = Modifier.padding(vertical = 14.dp),
              thickness = 1.dp,
              color = ModiBorder
            )

            // Section 1: Patient Profile
            ReportSectionTitle(title = "1. 환자 기본 정보")
            ReportDataRow(label = "성명 / 연령", value = "$userName ($userAge 세, 여성)")
            ReportDataRow(label = "배정 트랙", value = "[${assignedTrack.title}]")
            ReportDataRow(label = "주요 고민 부위", value = profile?.primaryConcernArea ?: "가르마 선 중심")

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.8.dp, color = ModiBorder)

            // Section 2: Safe Triage Red Flags
            ReportSectionTitle(title = "2. 안전 선별 (Safe Triage) 위험 신호")
            val hasRedFlag = profile?.dangerPatch == true || profile?.dangerPainErythema == true || profile?.dangerScarring == true
            if (hasRedFlag) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(ModiAlertLight)
                  .padding(8.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = ModiAlert,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "피부과 전문의 조기 면역/염증 감별 권고",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = ModiAlert
                  )
                }
              }
              Spacer(modifier = Modifier.height(8.dp))
            }
            ReportDataRow(label = "원형 탈모반", value = if (profile?.dangerPatch == true) "의심 관찰 (자가면역 감별 필요)" else "해당 없음")
            ReportDataRow(label = "두피 통증/염증", value = if (profile?.dangerPainErythema == true) "통증 및 붉은 발진 호소" else "특이소견 없음")
            ReportDataRow(label = "모공 소실(흉터성)", value = if (profile?.dangerScarring == true) "모공 폐쇄 의심 (골든타임 필수)" else "모공 보존됨")

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.8.dp, color = ModiBorder)

            // Section 3: Life Stage & Systemic Factors
            ReportSectionTitle(title = "3. 생애주기 및 전신 영양 상태")
            ReportDataRow(label = "출산 후 경과", value = if (profile?.isPostpartum == true) "출산 1년 미만 (산후 휴지기 탈모 의심)" else "해당 없음")
            ReportDataRow(label = "수유 상태", value = if (profile?.isLactating == true) "현재 모유 수유 중 (약물 흡수 주의 요망)" else "비수유")
            ReportDataRow(label = "철분/식이 결핍", value = if (profile?.isIronDeficient == true) "페리틴(저장철) 고갈 또는 빈혈 의심" else "양호")

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.8.dp, color = ModiBorder)

            // Section 4: History & Symptoms
            ReportSectionTitle(title = "4. 증상 지속 기간 및 기존 시도")
            ReportDataRow(label = "탈모 지속 기간", value = if (profile?.durationCategory == "1YEAR_MORE") "1년 이상 (만성 진행성 연모화)" else "6개월~1년 이내")
            ReportDataRow(label = "1일 탈모량 체감", value = "하루 약 ${profile?.sheddingPerDay ?: "50~100개"} 수준")
            ReportDataRow(label = "과거 관리 시도", value = profile?.pastTriedProducts ?: "탈모 샴푸, 비오틴 등")

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.8.dp, color = ModiBorder)

            // Section 5: Doctor Consultation Points
            ReportSectionTitle(title = "5. 전문의 상담 권장 항목")
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                text = "• 더모스코피(두피 확대경): 연모화 비율(Anisotrichosis) 및 다모낭 비율 측정",
                fontSize = 11.sp,
                color = ModiForest,
                lineHeight = 16.sp
              )
              Text(
                text = "• 혈액 검사 상담: 혈청 페리틴(Ferritin) 및 갑상선 호르몬(TSH) 수치 점검",
                fontSize = 11.sp,
                color = ModiForest,
                lineHeight = 16.sp
              )
              Text(
                text = "• 처방 외용제 상담: 가임기 및 수유 여부에 적합한 미녹시딜 농도(2~3%) 처방",
                fontSize = 11.sp,
                color = ModiForest,
                lineHeight = 16.sp
              )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Non-medical Disclaimer
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(ModiIvory)
                .padding(10.dp)
            ) {
              Text(
                text = "※ 본 문서는 의료법상 공식 의학적 진단서가 아니며, 환자가 직접 모디파이 앱을 통해 작성한 자가문진 데이터를 바탕으로 피부과 진료 시 의사와의 신속한 소통을 돕기 위해 생성된 사전 문진 참고 자료입니다.",
                fontSize = 10.sp,
                color = ModiTextSecondary,
                lineHeight = 14.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Copy & Share Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("MODIFI 진료 요약서", reportPlainText)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "리포트가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("copy_report_text_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = ModiForest,
              contentColor = ModiIvory
            )
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text("리포트 전체 복사", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
          }

          OutlinedButton(
            onClick = {
              Toast.makeText(context, "의사 선생님께 화면을 직접 보여주셔도 좋습니다.", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .weight(1f)
              .height(48.dp),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("진료실 직접 제시", fontSize = 13.sp, color = ModiForest, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
private fun ReportSectionTitle(title: String) {
  Text(
    text = title,
    fontSize = 12.5.sp,
    fontWeight = FontWeight.Bold,
    color = ModiForest,
    modifier = Modifier.padding(bottom = 6.dp)
  )
}

@Composable
private fun ReportDataRow(label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      fontSize = 11.5.sp,
      color = ModiTextSecondary,
      modifier = Modifier.weight(1f)
    )
    Text(
      text = value,
      fontSize = 11.5.sp,
      fontWeight = FontWeight.SemiBold,
      color = ModiCharcoal,
      modifier = Modifier.weight(1.4f),
      textAlign = androidx.compose.ui.text.style.TextAlign.End
    )
  }
}
