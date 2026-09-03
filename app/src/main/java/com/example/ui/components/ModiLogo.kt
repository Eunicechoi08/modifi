package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ModiForest
import com.example.ui.theme.ModiIvory
import com.example.ui.theme.ModiSage
import com.example.ui.theme.ModiTextSecondary

/**
 * MODI Brand Logo Symbol & Lockup
 * M = Me(나)
 * Hair Flow = 모발의 흐름과 나를 이해해가는 과정
 * Insight Dot = 나만의 상태를 발견하는 포인트
 */
@Composable
fun ModiLogoSymbol(
  modifier: Modifier = Modifier,
  size: Dp = 64.dp,
  strokeColor: Color = ModiForest,
  strokeWidth: Float = 4f
) {
  Canvas(modifier = modifier.size(size)) {
    val w = this.size.width
    val h = this.size.height

    // 1. Elegant Oval Enclosure
    val ovalWidth = w * 0.72f
    val ovalHeight = h * 0.90f
    val ovalLeft = (w - ovalWidth) / 2f
    val ovalTop = (h - ovalHeight) / 2f
    drawOval(
      color = strokeColor,
      topLeft = Offset(ovalLeft, ovalTop),
      size = Size(ovalWidth, ovalHeight),
      style = Stroke(width = strokeWidth)
    )

    // 2. Insight Dot (Top Center inside oval)
    val dotRadius = w * 0.042f
    drawCircle(
      color = strokeColor,
      radius = dotRadius,
      center = Offset(w * 0.5f, h * 0.22f)
    )

    // 3. 'M' (Me) Stem & Dip
    val mPath = Path().apply {
      moveTo(w * 0.35f, h * 0.68f)
      lineTo(w * 0.35f, h * 0.34f)
      lineTo(w * 0.50f, h * 0.54f)
      lineTo(w * 0.65f, h * 0.34f)
      lineTo(w * 0.65f, h * 0.68f)
    }
    drawPath(
      path = mPath,
      color = strokeColor,
      style = Stroke(
        width = strokeWidth * 0.9f,
        cap = StrokeCap.Round,
        join = StrokeJoin.Round
      )
    )

    // 4. Hair Flow & Sprout: curving upward to right
    val sproutPath = Path().apply {
      moveTo(w * 0.50f, h * 0.72f)
      cubicTo(
        w * 0.52f, h * 0.62f,
        w * 0.62f, h * 0.52f,
        w * 0.75f, h * 0.50f
      )
    }
    drawPath(
      path = sproutPath,
      color = strokeColor,
      style = Stroke(
        width = strokeWidth * 0.8f,
        cap = StrokeCap.Round
      )
    )

    // Small leaf shape
    val leafPath = Path().apply {
      moveTo(w * 0.58f, h * 0.60f)
      quadraticTo(w * 0.68f, h * 0.56f, w * 0.72f, h * 0.52f)
      quadraticTo(w * 0.68f, h * 0.62f, w * 0.59f, h * 0.62f)
      close()
    }
    drawPath(
      path = leafPath,
      color = strokeColor
    )
  }
}

@Composable
fun ModiAppIconCard(
  modifier: Modifier = Modifier,
  size: Dp = 80.dp,
  backgroundColor: Color = ModiForest,
  symbolColor: Color = ModiIvory
) {
  Box(
    modifier = modifier
      .size(size)
      .background(backgroundColor, RoundedCornerShape(size * 0.26f)),
    contentAlignment = Alignment.Center
  ) {
    ModiLogoSymbol(
      size = size * 0.72f,
      strokeColor = symbolColor,
      strokeWidth = 3.5f
    )
  }
}

@Composable
fun ModiBrandHeader(
  modifier: Modifier = Modifier,
  showSubtitle: Boolean = true
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    ModiLogoSymbol(
      size = 68.dp,
      strokeColor = ModiForest,
      strokeWidth = 4f
    )
    Spacer(modifier = Modifier.height(10.dp))
    Text(
      text = "MODIFI",
      fontSize = 26.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 5.sp,
      color = ModiForest,
      fontFamily = FontFamily.Serif
    )
    if (showSubtitle) {
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "모(毛)를 다르게 정의하고 바꾸다",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = ModiTextSecondary
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "毛(모) + Modify • Define Differently",
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        color = ModiSage,
        letterSpacing = 1.sp
      )
    }
  }
}
