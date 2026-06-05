package com.smartclipboardai.compose

/**
 * SmartClipboardAIApp - 메인 앱 컴포넌트
 *
 * 역할: 앱의 전체 구조와 네비게이션 관리
 *
 * 주요 기능:
 * - 화면 간 네비게이션 로직 (Home, Data, Tasks, History, Storage, AiSuggest, Analyzing, TopicDetail, ActionReview)
 * - 바텀 네비게이션 바 관리 (홈, 데이터, 작업)
 * - ModalBottomSheet: 분석 시작 확인 다이얼로그 (데이터 선택 후 AI 분석 시작)
 * - 각 화면을 조건부로 렌더링
 * - 데이터 선택 모드 상태 관리
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartClipboardAIApp() {
    var activeTab by remember { mutableStateOf(NavTab.Home) }
    var screen by remember { mutableStateOf(Screen.Home) }
    var navData by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var dataSelectMode by remember { mutableStateOf(false) }
    var bottomSheetVisible by remember { mutableStateOf(false) }
    var sheetCount by remember { mutableStateOf(0) }
    var sheetTopicName by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun navigate(target: Screen, data: Map<String, String> = emptyMap()) {
        screen = target
        navData = data
        when (target) {
            Screen.Home -> activeTab = NavTab.Home
            Screen.Data -> activeTab = NavTab.Data
            Screen.Tasks -> activeTab = NavTab.Tasks
            else -> Unit
        }
    }

    val showBottomBar = false

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(activeTab = activeTab) { tab ->
                    when (tab) {
                        NavTab.Home -> navigate(Screen.Home)
                        NavTab.Data -> navigate(Screen.Data)
                        NavTab.Tasks -> navigate(Screen.Tasks)
                    }
                }
            }
        },
        containerColor = AppColors.Surface,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AppColors.Surface),
        ) {
            when (screen) {
                Screen.Home -> HomeScreen(navigate = ::navigate)
                Screen.Data -> DataScreen(
                    navigate = ::navigate,
                    onSelectModeChange = { dataSelectMode = it },
                    onOpenSheet = { count, title ->
                        sheetCount = count
                        sheetTopicName = title
                        bottomSheetVisible = true
                    },
                )
                Screen.Tasks -> TasksScreen(navigate = ::navigate)
                Screen.History -> HistoryScreen(navigate = ::navigate)
                Screen.Storage -> StorageScreen(navigate = ::navigate)
                Screen.AiSuggest -> AiSuggestScreen(navigate = ::navigate, data = navData)
                Screen.Analyzing -> AnalyzingScreen(navigate = ::navigate, data = navData)
                Screen.TopicDetail -> TopicDetailScreen(navigate = ::navigate, data = navData)
                Screen.ActionReview -> ActionReviewScreen(navigate = ::navigate, data = navData)
            }
        }
    }

    if (bottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { bottomSheetVisible = false },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text("분석 시작", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = AppColors.Slate800)
                Text("${sheetCount}개의 데이터를 AI 에이전트로 분석합니다.", fontSize = 12.sp, color = AppColors.Slate500)
                OutlinedTextField(
                    value = sheetTopicName,
                    onValueChange = { sheetTopicName = it },
                    label = { Text("주제명") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                )
                GradientButton(
                    text = "분석",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        bottomSheetVisible = false
                        dataSelectMode = false
                        navigate(
                            Screen.Analyzing,
                            mapOf(
                                "selectedCount" to sheetCount.toString(),
                                "topicName" to sheetTopicName,
                            ),
                        )
                    },
                )
                Button(
                    onClick = { bottomSheetVisible = false },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AppColors.Surface, contentColor = AppColors.Slate500),
                    border = BorderStroke(1.dp, AppColors.Slate200),
                ) {
                    Text("취소")
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun BottomNavigation(activeTab: NavTab, onSelect: (NavTab) -> Unit) {
    NavigationBar(
        containerColor = Color.White.copy(alpha = 0.98f),
        tonalElevation = 8.dp,
    ) {
        val items = listOf(
            NavTab.Home to ("홈" to Icons.Default.Home),
            NavTab.Data to ("데이터" to Icons.Default.Storage),
            NavTab.Tasks to ("작업" to Icons.Default.Work),
        )
        items.forEach { (tab, pair) ->
            NavigationBarItem(
                selected = activeTab == tab,
                onClick = { onSelect(tab) },
                icon = { Icon(pair.second, contentDescription = pair.first) },
                label = { Text(pair.first, fontSize = 11.sp) },
            )
        }
    }
}
