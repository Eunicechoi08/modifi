package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AssessmentScreen
import com.example.ui.screens.DoctorReportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyPageScreen
import com.example.ui.screens.PhotoLogScreen
import com.example.ui.screens.TrackResultScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.ModiBorder
import com.example.ui.theme.ModiForest
import com.example.ui.theme.ModiIvory
import com.example.ui.theme.ModiRose
import com.example.ui.theme.ModiSage
import com.example.ui.theme.ModiSageLight
import com.example.ui.theme.ModiTextSecondary
import com.example.ui.theme.ModiTheme
import com.example.ui.viewmodel.ModiViewModel

sealed class AppDestination {
  data object Welcome : AppDestination()
  data object Assessment : AppDestination()
  data object TrackResult : AppDestination()
  data object Home : AppDestination()
  data object PhotoLog : AppDestination()
  data object DoctorReport : AppDestination()
  data object MyPage : AppDestination()
}

enum class BottomTab(
  val label: String,
  val activeIcon: ImageVector,
  val inactiveIcon: ImageVector,
  val testTag: String
) {
  HOME("버디 루틴", Icons.Filled.Home, Icons.Outlined.Home, "tab_home"),
  PHOTO_LOG("변화 기록", Icons.Filled.Spa, Icons.Outlined.Spa, "tab_photo_log"),
  DOCTOR_REPORT("진료 리포트", Icons.Filled.MedicalServices, Icons.Outlined.MedicalServices, "tab_doctor_report"),
  MY_PAGE("내 모디파이", Icons.Filled.Person, Icons.Outlined.Person, "tab_my_page")
}

class MainActivity : ComponentActivity() {
  private val viewModel: ModiViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      ModiTheme {
        ModiMainApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ModiMainApp(viewModel: ModiViewModel) {
  val profile by viewModel.profile.collectAsState()

  // Navigation Destination State
  var currentDestination by remember {
    mutableStateOf<AppDestination>(
      if (profile?.isCompleted == true) AppDestination.Home else AppDestination.Welcome
    )
  }

  // Synchronize when profile becomes available initially
  if (profile?.isCompleted == true && currentDestination == AppDestination.Welcome) {
    currentDestination = AppDestination.Home
  }

  // Active Bottom Tab
  val activeBottomTab = when (currentDestination) {
    AppDestination.Home -> BottomTab.HOME
    AppDestination.PhotoLog -> BottomTab.PHOTO_LOG
    AppDestination.DoctorReport -> BottomTab.DOCTOR_REPORT
    AppDestination.MyPage -> BottomTab.MY_PAGE
    else -> null
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .windowInsetsPadding(WindowInsets.safeDrawing),
    bottomBar = {
      if (activeBottomTab != null) {
        NavigationBar(
          containerColor = Color.White,
          tonalElevation = 8.dp,
          modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("main_bottom_nav_bar")
        ) {
          BottomTab.entries.forEach { tab ->
            val isSelected = activeBottomTab == tab
            NavigationBarItem(
              selected = isSelected,
              onClick = {
                currentDestination = when (tab) {
                  BottomTab.HOME -> AppDestination.Home
                  BottomTab.PHOTO_LOG -> AppDestination.PhotoLog
                  BottomTab.DOCTOR_REPORT -> AppDestination.DoctorReport
                  BottomTab.MY_PAGE -> AppDestination.MyPage
                }
              },
              icon = {
                Icon(
                  imageVector = if (isSelected) tab.activeIcon else tab.inactiveIcon,
                  contentDescription = tab.label,
                  modifier = Modifier.size(22.dp)
                )
              },
              label = {
                Text(
                  text = tab.label,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ModiForest,
                selectedTextColor = ModiForest,
                indicatorColor = ModiSageLight,
                unselectedIconColor = ModiTextSecondary,
                unselectedTextColor = ModiTextSecondary
              ),
              modifier = Modifier.testTag(tab.testTag)
            )
          }
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(ModiIvory)
    ) {
      AnimatedContent(
        targetState = currentDestination,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screen_transition"
      ) { destination ->
        when (destination) {
          AppDestination.Welcome -> {
            WelcomeScreen(
              onStartAssessment = { currentDestination = AppDestination.Assessment },
              onContinueToHome = if (profile?.isCompleted == true) {
                { currentDestination = AppDestination.Home }
              } else null
            )
          }

          AppDestination.Assessment -> {
            AssessmentScreen(
              viewModel = viewModel,
              onNavigateBack = {
                currentDestination = if (profile?.isCompleted == true) AppDestination.Home else AppDestination.Welcome
              },
              onCompleteAssessment = {
                currentDestination = AppDestination.TrackResult
              }
            )
          }

          AppDestination.TrackResult -> {
            TrackResultScreen(
              viewModel = viewModel,
              onProceedToHome = {
                currentDestination = AppDestination.Home
              }
            )
          }

          AppDestination.Home -> {
            HomeScreen(
              viewModel = viewModel,
              onNavigateToPhotoLog = { currentDestination = AppDestination.PhotoLog },
              onNavigateToDoctorReport = { currentDestination = AppDestination.DoctorReport },
              onNavigateToMyPage = { currentDestination = AppDestination.MyPage }
            )
          }

          AppDestination.PhotoLog -> {
            PhotoLogScreen(
              viewModel = viewModel,
              onNavigateBack = { currentDestination = AppDestination.Home }
            )
          }

          AppDestination.DoctorReport -> {
            DoctorReportScreen(
              viewModel = viewModel,
              onNavigateBack = { currentDestination = AppDestination.Home }
            )
          }

          AppDestination.MyPage -> {
            MyPageScreen(
              viewModel = viewModel,
              onNavigateBack = { currentDestination = AppDestination.Home },
              onRetakeAssessment = {
                viewModel.resetSurvey()
                currentDestination = AppDestination.Assessment
              }
            )
          }
        }
      }
    }
  }
}
