package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.HairPhotoLogEntity
import com.example.ui.theme.ModiAlert
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
fun PhotoLogScreen(
  viewModel: ModiViewModel,
  onNavigateBack: () -> Unit
) {
  val photoLogs by viewModel.photoLogs.collectAsState()
  var ghostOpacity by remember { mutableFloatStateOf(0.5f) }
  var showAddLogDialog by remember { mutableStateOf(false) }
  var showGuideDialog by remember { mutableStateOf(false) }
  var selectedArea by remember { mutableStateOf("가르마 선") }

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
          modifier = Modifier.testTag("photo_log_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "뒤로가기",
            tint = ModiForest
          )
        }

        Text(
          text = "고스트 겹침 변화 기록",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = ModiForest
        )

        IconButton(onClick = { showGuideDialog = true }) {
          Icon(
            imageVector = Icons.Default.HelpOutline,
            contentDescription = "촬영 가이드",
            tint = ModiForest
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(scrollState)
      ) {
        // Area Selector Chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("가르마 선", "정수리", "헤어라인").forEach { area ->
            val isSelected = selectedArea == area
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) ModiForest else Color.White)
                .border(
                  width = 1.dp,
                  color = if (isSelected) ModiForest else ModiBorder,
                  shape = RoundedCornerShape(10.dp)
                )
                .clickable { selectedArea = area }
                .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
              Text(
                text = area,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) ModiIvory else ModiTextSecondary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------------------------------------
        // Ghost Overlay Viewfinder Canvas (고스트 뷰파인더)
        // -------------------------------------------------------------
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
          colors = CardDefaults.cardColors(containerColor = ModiForestDark),
          shape = RoundedCornerShape(20.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Layers,
                  contentDescription = null,
                  tint = ModiSage,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "고스트 겹침 뷰파인더",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = ModiIvory
                )
              }
              Text(
                text = "투명도 ${(ghostOpacity * 100).toInt()}%",
                fontSize = 11.sp,
                color = ModiSageLight
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Camera Viewfinder Screen with Ghost Guidelines
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.25f)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF16211C)),
              contentAlignment = Alignment.Center
            ) {
              // Simulated hairline & scalp rendering with ghost opacity
              GhostHairCanvas(ghostAlpha = ghostOpacity, area = selectedArea)

              // Center Target Indicator
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .border(1.5.dp, ModiRose.copy(alpha = 0.8f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Box(
                  modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(ModiRose)
                )
              }

              // Tip badge
              Box(
                modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .padding(bottom = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "가운데 세로 가이드라인에 가르마 선을 정렬하세요",
                  fontSize = 10.sp,
                  color = ModiIvory
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Opacity Slider
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "현재 시야",
                fontSize = 10.5.sp,
                color = ModiSageLight
              )
              Slider(
                value = ghostOpacity,
                onValueChange = { ghostOpacity = it },
                modifier = Modifier
                  .weight(1f)
                  .padding(horizontal = 8.dp),
                colors = SliderDefaults.colors(
                  thumbColor = ModiRose,
                  activeTrackColor = ModiSage,
                  inactiveTrackColor = Color.Gray.copy(alpha = 0.4f)
                )
              )
              Text(
                text = "이전 사진 겹침",
                fontSize = 10.5.sp,
                color = ModiSageLight
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action: Snap / Add photo log button
            Button(
              onClick = { showAddLogDialog = true },
              modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("snap_photo_log_button"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = ModiSage,
                contentColor = ModiForestDark
              )
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CameraAlt,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "이 위치로 변화 기록 저장하기",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------------------------------------------------
        // Before & After Comparative Section
        // -------------------------------------------------------------
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "주차별 가르마 변화 아카이브",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
          Text(
            text = "총 ${photoLogs.size}회 기록",
            fontSize = 12.sp,
            color = ModiTextSecondary
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (photoLogs.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color.White)
              .border(1.dp, ModiBorder, RoundedCornerShape(14.dp))
              .padding(24.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null,
                tint = ModiSage,
                modifier = Modifier.size(32.dp)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "아직 등록된 두피 기록이 없습니다.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ModiCharcoal
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "상단의 '변화 기록 저장하기' 버튼으로 첫 가르마 기준 사진을 남겨보세요.",
                fontSize = 11.5.sp,
                color = ModiTextSecondary,
                textAlign = TextAlign.Center
              )
            }
          }
        } else {
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            photoLogs.forEach { log ->
              PhotoLogItemCard(
                log = log,
                onDelete = { viewModel.deletePhotoLog(log.id) }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }

  // -----------------------------------------------------------------
  // Add Photo Log Dialog
  // -----------------------------------------------------------------
  if (showAddLogDialog) {
    var noteText by remember { mutableStateOf("") }
    var sheddingRating by remember { mutableStateOf("안정됨") }
    var densityRating by remember { mutableIntStateOf(3) }

    AlertDialog(
      onDismissRequest = { showAddLogDialog = false },
      confirmButton = {
        Button(
          onClick = {
            viewModel.addPhotoLog(
              targetArea = selectedArea,
              sheddingRating = sheddingRating,
              densityRating = densityRating,
              note = noteText.ifBlank { "고스트 겹침 가이드로 촬영한 정기 기록" }
            )
            showAddLogDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = ModiForest)
        ) {
          Text("기록 저장", color = ModiIvory, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddLogDialog = false }) {
          Text("취소", color = ModiTextSecondary)
        }
      },
      title = {
        Text(
          text = "${selectedArea} 변화 일지 기록",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = ModiForest
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
          Text(
            text = "오늘의 빠짐 체감과 두피 상태를 체크해 주세요.",
            fontSize = 12.sp,
            color = ModiTextSecondary
          )

          // Shedding Selector
          Text(
            text = "머리 빠짐 체감:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            listOf("안정됨", "평소 수준", "쉐딩기 일시증가").forEach { option ->
              val isSelected = sheddingRating == option
              Box(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (isSelected) ModiForest else ModiIvory)
                  .border(
                    1.dp,
                    if (isSelected) ModiForest else ModiBorder,
                    RoundedCornerShape(8.dp)
                  )
                  .clickable { sheddingRating = option }
                  .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = option,
                  fontSize = 10.5.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) ModiIvory else ModiCharcoal
                )
              }
            }
          }

          // Density Rating Stars
          Text(
            text = "가르마 숱/밀도 만족도 (1~5점):",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ModiForest
          )
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            (1..5).forEach { star ->
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (star <= densityRating) ModiRose else ModiBorder,
                modifier = Modifier
                  .size(28.dp)
                  .clickable { densityRating = star }
              )
            }
          }

          // Note Field
          OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            placeholder = { Text("두피 열감, 잔머리 관찰 등 메모") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )
        }
      },
      shape = RoundedCornerShape(16.dp),
      containerColor = Color.White
    )
  }

  // -----------------------------------------------------------------
  // Guide Dialog
  // -----------------------------------------------------------------
  if (showGuideDialog) {
    AlertDialog(
      onDismissRequest = { showGuideDialog = false },
      confirmButton = {
        TextButton(onClick = { showGuideDialog = false }) {
          Text("이해했어요", color = ModiForest, fontWeight = FontWeight.Bold)
        }
      },
      title = {
        Text(
          text = "고스트 겹침 촬영 3대 원칙",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = ModiForest
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "1. 동일한 조명 아래에서:\n형광등 바로 아래나 자연광 창가 등 매번 같은 밝기에서 촬영하세요.\n\n" +
              "2. 정수리 각도 고정:\n스마트폰을 머리 정수리에서 수직 30cm 거리에 두고, 중앙 빨간 점을 가르마 시작점에 맞추세요.\n\n" +
              "3. 투명도 슬라이더 활용:\n이전 기록과 겹쳐보면서 가르마 폭이 1mm씩 좁혀지는 미세 잔디 모발을 눈으로 확인하세요.",
            fontSize = 12.sp,
            color = ModiCharcoal,
            lineHeight = 17.sp
          )
        }
      },
      shape = RoundedCornerShape(16.dp),
      containerColor = Color.White
    )
  }
}

// Visual drawing of scalp & part line for ghost overlay
@Composable
private fun GhostHairCanvas(ghostAlpha: Float, area: String) {
  Canvas(modifier = Modifier.fillMaxSize()) {
    val w = size.width
    val h = size.height
    val cx = w / 2f
    val cy = h / 2f

    // 1. Center Part Line (Solid or dashed guide)
    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), 0f)
    drawLine(
      color = ModiSage.copy(alpha = 0.8f),
      start = Offset(cx, h * 0.12f),
      end = Offset(cx, h * 0.88f),
      strokeWidth = 2.5f,
      pathEffect = dashEffect
    )

    // Horizontal guideline
    drawLine(
      color = Color.White.copy(alpha = 0.25f),
      start = Offset(w * 0.2f, cy),
      end = Offset(w * 0.8f, cy),
      strokeWidth = 1.5f
    )

    // Scalp contour ellipse
    drawOval(
      color = ModiSage.copy(alpha = 0.35f),
      topLeft = Offset(w * 0.25f, h * 0.18f),
      size = Size(w * 0.50f, h * 0.64f),
      style = Stroke(width = 1.8f)
    )

    // Ghost baseline hair strands (simulated previous capture overlay)
    val ghostColor = ModiRose.copy(alpha = ghostAlpha * 0.75f)
    drawOval(
      color = ghostColor,
      topLeft = Offset(w * 0.30f, h * 0.26f),
      size = Size(w * 0.40f, h * 0.48f),
      style = Stroke(width = 2.2f)
    )

    // Multiple delicate hair flow curves
    val hairStrandColor = Color(0xFFC7D5C9).copy(alpha = (1f - ghostAlpha * 0.5f).coerceIn(0.2f, 0.9f))
    for (i in -4..4) {
      if (i == 0) continue
      val offset = i * (w * 0.045f)
      drawLine(
        color = hairStrandColor,
        start = Offset(cx + offset * 0.4f, cy - h * 0.20f),
        end = Offset(cx + offset * 1.6f, cy + h * 0.26f),
        strokeWidth = 1.5f
      )
    }
  }
}

@Composable
private fun PhotoLogItemCard(
  log: HairPhotoLogEntity,
  onDelete: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(Color.White)
      .border(1.dp, ModiBorder, RoundedCornerShape(14.dp))
      .padding(14.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        // Thumbnail icon box
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(ModiForestDark),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.PhotoLibrary,
              contentDescription = null,
              tint = ModiSage,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = log.targetArea.take(3),
              fontSize = 9.sp,
              color = ModiIvory
            )
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = log.dateStr,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = ModiCharcoal
            )
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(ModiSageLight)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = log.sheddingRating,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = ModiForest
              )
            }
          }

          Spacer(modifier = Modifier.height(3.dp))

          // Star ratings
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "밀도 체감:",
              fontSize = 11.sp,
              color = ModiTextSecondary
            )
            Spacer(modifier = Modifier.width(4.dp))
            (1..5).forEach { star ->
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (star <= log.densityRating) ModiRose else ModiBorder,
                modifier = Modifier.size(13.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = log.note,
            fontSize = 11.5.sp,
            color = ModiTextSecondary,
            lineHeight = 15.sp
          )
        }
      }

      IconButton(onClick = onDelete) {
        Icon(
          imageVector = Icons.Default.DeleteOutline,
          contentDescription = "삭제",
          tint = ModiBorder,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}
