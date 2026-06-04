package com.smartclipboardai.compose

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartClipboardAITheme {
                SmartClipboardAIApp()
            }
        }
    }
}

private object AppColors {
    val Blue = Color(0xFF1D4ED8)
    val BlueDeep = Color(0xFF1E3A8A)
    val BlueSoft = Color(0xFFEFF6FF)
    val Cyan = Color(0xFF0891B2)
    val Green = Color(0xFF059669)
    val Slate900 = Color(0xFF0F172A)
    val Slate800 = Color(0xFF1E293B)
    val Slate500 = Color(0xFF64748B)
    val Slate400 = Color(0xFF94A3B8)
    val Slate200 = Color(0xFFE2E8F0)
    val Surface = Color(0xFFF8FAFC)
    val Border = Color(0xFFE8EDF8)
    val Red = Color(0xFFDC2626)
}

private val BlueGradient = Brush.linearGradient(listOf(AppColors.BlueDeep, AppColors.Blue, Color(0xFF3B82F6)))
private val DarkGradient = Brush.linearGradient(listOf(Color(0xFF0F1F3D), Color(0xFF1A3660), AppColors.BlueDeep))

enum class Screen { Home, Data, Tasks, TopicDetail, ActionReview }
enum class NavTab { Home, Data, Tasks }
enum class PermissionStatus { Unknown, Selecting, Granted, Partial, Denied }

data class ClipboardItem(
    val id: String,
    val name: String,
    val type: String,
    val mime: String,
    val date: String,
    val color: Color,
    val label: String,
    val preview: String,
)

data class Topic(
    val id: String,
    val title: String,
    val items: Int,
    val lastUpdated: String,
    val color: Color,
    val tags: List<String>,
)

data class ActionConfig(
    val key: String,
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val app: String,
    val appBg: Color,
    val defaultTitle: String,
    val defaultBody: String,
)

data class ChatMessage(val id: String, val role: MessageRole, val text: String)
enum class MessageRole { Ai, User }
data class DraftVersion(val id: Int, val label: String, val title: String, val body: String)

private val sampleItems = listOf(
    ClipboardItem("1", "Screenshot_20260526_091234_Meeting.jpg", "스크린샷", "image/jpeg", "5월 26일 09:12", AppColors.Blue, "회의 자료", "주간 업무 보고, Q2 목표 달성률 78%, 대비 현황 요약"),
    ClipboardItem("2", "Screenshot_20260524_133021_Travel.jpg", "스크린샷", "image/jpeg", "5월 24일 13:30", AppColors.BlueDeep, "여행 계획", "제주도 3박 4일, 애월 숙소, 렌터카 예약 완료"),
    ClipboardItem("3", "Screenshot_20260523_080045_Recipe.jpg", "스크린샷", "image/jpeg", "5월 23일 08:00", AppColors.Cyan, "레시피", "된장찌개 재료: 된장, 두부, 호박, 대파"),
    ClipboardItem("4", "Screenshot_20260522_195532_Event.jpg", "스크린샷", "image/jpeg", "5월 22일 19:55", AppColors.Green, "행사 안내", "사내 워크숍 안내, 5월 30일 오후 2시, 본사 B동 3층"),
    ClipboardItem("5", "Screenshot_20260520_164512_Test.jpg", "스크린샷", "image/jpeg", "5월 20일 16:45", AppColors.Blue, "테스트", "UI 컴포넌트 레이아웃 테스트, 버튼 정렬과 여백 확인"),
)

private val actionConfigs = mapOf(
    "note" to ActionConfig(
        key = "note",
        title = "요약 노트 초안",
        icon = Icons.Default.Description,
        color = AppColors.Blue,
        app = "Samsung Notes",
        appBg = Color(0xFFFFF9DB),
        defaultTitle = "스크린샷 수집 요약 노트",
        defaultBody = "수집한 스크린샷 분석 결과\n\n" +
            "• 회의 자료: 주간 업무 보고, Q2 목표 달성률 78%\n" +
            "• 여행 계획: 제주도 3박 4일, 애월 숙소 예약\n" +
            "• 레시피: 된장찌개 재료 목록\n" +
            "• 행사 안내: 워크숍 5월 30일 오후 2시\n" +
            "• 테스트: UI 컴포넌트 레이아웃 확인",
    ),
    "calendar" to ActionConfig(
        key = "calendar",
        title = "캘린더 일정 초안",
        icon = Icons.Default.CalendarMonth,
        color = Color(0xFF2563EB),
        app = "Samsung Calendar",
        appBg = AppColors.BlueSoft,
        defaultTitle = "사내 워크숍 - 5월 30일",
        defaultBody = "일정 정보\n\n제목: 사내 워크숍\n날짜: 2026년 5월 30일\n시간: 오후 2:00 ~ 오후 5:00\n장소: 본사 B동 3층 대회의실\n\n추가 일정\n• 제주도 여행: 6월 3일 ~ 6월 6일",
    ),
    "reminder" to ActionConfig(
        key = "reminder",
        title = "리마인더 초안",
        icon = Icons.Default.Notifications,
        color = AppColors.BlueDeep,
        app = "Samsung Reminders",
        appBg = Color(0xFFF5F3FF),
        defaultTitle = "워크숍 준비 및 재료 구매",
        defaultBody = "알림 목록\n\n• 된장찌개 재료 구매 - 5월 28일 오전 11:00\n• 워크숍 참석 확인 메일 발송 - 5월 29일 오전 9:00\n• 제주도 렌터카 최종 확인 - 6월 2일 오전 10:00",
    ),
    "share" to ActionConfig(
        key = "share",
        title = "공유 초안",
        icon = Icons.Default.Share,
        color = AppColors.Slate500,
        app = "공유하기",
        appBg = AppColors.Surface,
        defaultTitle = "스크린샷 모음 공유",
        defaultBody = "최근 수집한 정보를 정리했습니다.\n\n공유 내용을 편집하세요.",
    ),
)

@Composable
fun SmartClipboardAITheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = androidx.compose.material3.lightColorScheme(
            primary = AppColors.Blue,
            secondary = AppColors.BlueDeep,
            background = AppColors.Surface,
            surface = Color.White,
            onSurface = AppColors.Slate800,
        ),
        content = content,
    )
}

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

    val showBottomBar = screen in listOf(Screen.Home, Screen.Data, Screen.Tasks) && !dataSelectMode

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
                Text("${sheetCount}개의 데이터를 AI Agent로 분석합니다.", fontSize = 12.sp, color = AppColors.Slate500)
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
                        navigate(Screen.TopicDetail, mapOf("topicId" to "1"))
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
private fun BottomNavigation(activeTab: NavTab, onSelect: (NavTab) -> Unit) {
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

@Composable
private fun HomeScreen(navigate: (Screen, Map<String, String>) -> Unit) {
    var query by remember { mutableStateOf("") }
    var dashboardMode by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(false) }
    var showRecommend by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (showDetails || dashboardMode) BlueGradient else SolidColor(Color.White))
                    .padding(horizontal = 20.dp)
                    .padding(top = if (showDetails || dashboardMode) 20.dp else 48.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (!showDetails || dashboardMode) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = if (dashboardMode) Color.White else AppColors.Blue,
                        modifier = Modifier.size(if (dashboardMode) 28.dp else 54.dp),
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "SmartClipboardAI",
                        fontSize = if (dashboardMode) 18.sp else 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (dashboardMode) Color.White else AppColors.Slate800,
                    )
                    Text(
                        "수집한 정보를 AI로 정리하고 실행합니다",
                        fontSize = 12.sp,
                        color = if (dashboardMode) Color.White.copy(alpha = 0.72f) else AppColors.Slate400,
                    )
                    Spacer(Modifier.height(if (dashboardMode) 16.dp else 36.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("SmartClipboardAI", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(Modifier.height(16.dp))
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = if (showDetails) Color.White.copy(alpha = 0.10f) else Color.Transparent),
                    shape = RoundedCornerShape(20.dp),
                    border = if (showDetails) BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)) else null,
                ) {
                    Column(
                        modifier = Modifier
                            .background(if (showDetails) SolidColor(Color.Transparent) else BlueGradient)
                            .padding(20.dp),
                    ) {
                        Text("무엇을 정리할까요?", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("원하는 주제를 입력하거나 AI에게 맡겨보세요.", color = Color(0xFFC7D2FE).copy(alpha = 0.78f), fontSize = 11.sp)
                        Spacer(Modifier.height(16.dp))
                        TextField(
                            value = query,
                            onValueChange = { query = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("예: 회의 자료, 여행 링크, 일정 캡처", color = Color.White.copy(alpha = 0.55f), fontSize = 13.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White.copy(alpha = 0.14f),
                                unfocusedContainerColor = Color.White.copy(alpha = 0.14f),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                            ),
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { dashboardMode = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = AppColors.Blue),
                            ) {
                                Icon(Icons.Default.Psychology, null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("AI 찾기", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { dashboardMode = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.18f), contentColor = Color.White),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.28f)),
                            ) {
                                Text("직접 고르기", fontSize = 13.sp)
                            }
                        }
                        if (!dashboardMode) {
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = { showDetails = !showDetails },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.08f), contentColor = Color(0xFFC7D2FE)),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
                            ) {
                                Icon(
                                    imageVector = if (showDetails) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight,
                                    contentDescription = null,
                                    modifier = Modifier.rotate(if (showDetails) 180f else 90f),
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(if (showDetails) "수집 현황 닫기" else "수집 현황 보기", fontSize = 12.sp)
                                if (!showDetails) {
                                    Spacer(Modifier.width(6.dp))
                                    Pill("5개", bg = Color.White.copy(alpha = 0.14f), color = Color.White.copy(alpha = 0.9f))
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!dashboardMode && !showDetails) {
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DividerLine(Modifier.weight(1f))
                    Text("오늘 5개의 항목을 수집했어요", modifier = Modifier.padding(horizontal = 12.dp), color = AppColors.Slate400, fontSize = 10.sp)
                    DividerLine(Modifier.weight(1f))
                }
            }
        }

        if (showDetails || dashboardMode) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    DashboardSections(
                        showRecommend = showRecommend,
                        onDismissRecommend = { showRecommend = false },
                        navigate = navigate,
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardSections(
    showRecommend: Boolean,
    onDismissRecommend: () -> Unit,
    navigate: (Screen, Map<String, String>) -> Unit,
) {
    CardBlock {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF10B981)))
                    Spacer(Modifier.width(6.dp))
                    Text("오늘 수집", color = AppColors.Slate500, fontSize = 11.sp)
                }
                Text("5개 수집됨", color = AppColors.Slate800, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = { navigate(Screen.Data, emptyMap()) }) {
                Text("전체 보기", fontSize = 11.sp)
                Icon(Icons.Default.KeyboardArrowRight, null, modifier = Modifier.size(14.dp))
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
            StatChip(Icons.Default.Link, "링크 0", AppColors.Slate400)
            StatChip(Icons.Default.Description, "메모 0", AppColors.Slate400)
            StatChip(Icons.Default.CameraAlt, "스크린샷 5", AppColors.Blue)
            StatChip(Icons.Default.Image, "이미지 0", AppColors.Slate400)
        }
        Spacer(Modifier.height(10.dp))
        Text("수집한 데이터로 요약, 일정, 할 일 등의 추천 작업을 만들 수 있어요.", color = AppColors.Slate400, fontSize = 10.sp)
    }

    AnimatedVisibility(showRecommend) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, null, tint = AppColors.BlueDeep, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("추천 작업", color = AppColors.Slate800, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                TextButton(onClick = onDismissRecommend) {
                    Icon(Icons.Default.VisibilityOff, null, modifier = Modifier.size(12.dp), tint = AppColors.Slate400)
                    Spacer(Modifier.width(4.dp))
                    Text("숨기기", fontSize = 10.sp, color = AppColors.Slate400)
                }
            }
            CardBlock {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconBubble(Icons.Default.CameraAlt, AppColors.Blue, AppColors.BlueSoft, 44)
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Screenshot collection", color = AppColors.Slate800, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.width(6.dp))
                            Pill("AI 추천", Color(0xFFDBEAFE), AppColors.BlueDeep)
                        }
                        Text("스크린샷 5개를 시각 인사이트 문서로 정리할 수 있어요.", color = AppColors.Slate500, fontSize = 11.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("screenshots · 5개 · 60%", color = AppColors.Slate400, fontSize = 10.sp, modifier = Modifier.weight(1f))
                            GradientButton(text = "검토", compact = true) { navigate(Screen.TopicDetail, mapOf("topicId" to "1")) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DataScreen(
    navigate: (Screen, Map<String, String>) -> Unit,
    onSelectModeChange: (Boolean) -> Unit,
    onOpenSheet: (Int, String) -> Unit,
) {
    var activeFilter by remember { mutableStateOf("전체") }
    var items by remember { mutableStateOf(sampleItems) }
    var selectMode by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var deleteTargetId by remember { mutableStateOf<String?>(null) }
    var previewItem by remember { mutableStateOf<ClipboardItem?>(null) }
    var permission by remember { mutableStateOf(PermissionStatus.Unknown) }
    var pickerSelected by remember { mutableStateOf(sampleItems.map { it.id }.toSet()) }
    var allowedIds by remember { mutableStateOf<Set<String>>(emptySet()) }

    fun enterSelect() {
        selectMode = true
        selected = emptySet()
        onSelectModeChange(true)
    }
    fun exitSelect() {
        selectMode = false
        selected = emptySet()
        onSelectModeChange(false)
    }

    BackHandler(selectMode) { exitSelect() }
    LaunchedEffect(selectMode) { onSelectModeChange(selectMode) }

    val visibleItems = when (permission) {
        PermissionStatus.Unknown, PermissionStatus.Denied, PermissionStatus.Selecting -> emptyList()
        PermissionStatus.Partial -> items.filter { it.id in allowedIds }
        PermissionStatus.Granted -> items
    }
    val filters = listOf("전체", "메모", "링크", "이미지", "파일", "스크린샷")
    val filtered = if (activeFilter == "전체") visibleItems else visibleItems.filter { it.type == activeFilter }

    if (permission == PermissionStatus.Selecting) {
        PhotoPermissionPicker(
            selected = pickerSelected,
            onToggle = { id -> pickerSelected = if (id in pickerSelected) pickerSelected - id else pickerSelected + id },
            onSelectAll = { pickerSelected = sampleItems.map { it.id }.toSet() },
            onClear = { pickerSelected = emptySet() },
            onCancel = { permission = PermissionStatus.Unknown },
            onDone = {
                allowedIds = pickerSelected
                permission = PermissionStatus.Partial
            },
        )
        return
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            DataHeader(
                selectMode = selectMode,
                selectedCount = selected.size,
                visibleCount = visibleItems.size,
                onDeleteAll = { showDeleteConfirm = true },
                onDeleteSelected = {
                    items = items.filterNot { it.id in selected }
                    exitSelect()
                },
                onToggleSelect = { if (selectMode) exitSelect() else enterSelect() },
            )

            if (showDeleteConfirm) {
                ConfirmBanner(
                    text = "수집한 데이터 ${visibleItems.size}개를 모두 삭제할까요?",
                    onConfirm = {
                        items = emptyList()
                        showDeleteConfirm = false
                    },
                    onCancel = { showDeleteConfirm = false },
                )
            }
            deleteTargetId?.let { id ->
                ConfirmBanner(
                    text = "이 항목을 삭제할까요?",
                    onConfirm = {
                        items = items.filterNot { it.id == id }
                        deleteTargetId = null
                    },
                    onCancel = { deleteTargetId = null },
                )
            }

            if (!selectMode) {
                PermissionCard(
                    permission = permission,
                    allowedCount = allowedIds.size,
                    onGrantAll = { permission = PermissionStatus.Granted },
                    onGrantPartial = { permission = PermissionStatus.Selecting },
                    onDeny = { permission = PermissionStatus.Denied },
                    onReset = { permission = PermissionStatus.Unknown },
                )
            }

            if (selectMode) {
                SelectModeBar(
                    selectedCount = selected.size,
                    onSelectAll = { selected = filtered.map { it.id }.toSet() },
                    onClear = { selected = emptySet() },
                )
            }

            FilterRow(filters, activeFilter, visibleItems.size) { activeFilter = it }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (filtered.isEmpty()) {
                    item { EmptyDataState(permission) }
                } else {
                    items(filtered, key = { it.id }) { item ->
                        DataItemCard(
                            item = item,
                            selected = item.id in selected,
                            selectMode = selectMode,
                            onToggle = {
                                if (selectMode) selected = if (item.id in selected) selected - item.id else selected + item.id
                            },
                            onPreview = { previewItem = item },
                            onDelete = { deleteTargetId = item.id },
                        )
                    }
                }
            }

            if (selectMode && selected.isNotEmpty()) {
                Surface(shadowElevation = 8.dp, color = Color.White) {
                    GradientButton(
                        text = "${selected.size}개 선택 완료",
                        icon = Icons.Default.KeyboardArrowRight,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        onOpenSheet(selected.size, "Collected items (${selected.size})")
                    }
                }
            }
        }

        previewItem?.let { item ->
            PreviewOverlay(item = item, onClose = { previewItem = null })
        }
    }
}

@Composable
private fun PhotoPermissionPicker(
    selected: Set<String>,
    onToggle: (String) -> Unit,
    onSelectAll: () -> Unit,
    onClear: () -> Unit,
    onCancel: () -> Unit,
    onDone: () -> Unit,
) {
    Column(Modifier.fillMaxSize().background(Color.White)) {
        HeaderRow(
            title = "사진 선택",
            subtitle = "허용할 사진을 선택하세요",
            leading = Icons.Default.Close,
            onLeading = onCancel,
            action = {
                GradientButton(text = "완료 ${if (selected.isNotEmpty()) "(${selected.size}개)" else ""}", compact = true, enabled = selected.isNotEmpty(), onClick = onDone)
            },
        )
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFFAFBFF))
                .border(1.dp, Color(0xFFF1F5F9))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("${selected.size}/${sampleItems.size}개 선택됨", color = AppColors.Slate500, fontSize = 11.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("모두 선택", color = AppColors.Blue, fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.clickable(onClick = onSelectAll))
                Text("선택 해제", color = AppColors.Slate400, fontSize = 11.sp, modifier = Modifier.clickable(onClick = onClear))
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(sampleItems) { item ->
                val isSelected = item.id in selected
                Column(
                    Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .border(if (isSelected) 2.dp else 1.dp, if (isSelected) AppColors.Blue else AppColors.Border, RoundedCornerShape(18.dp))
                        .clickable { onToggle(item.id) }
                        .background(Color.White),
                ) {
                    Thumbnail(item, modifier = Modifier.height(104.dp), showCheck = true, checked = isSelected)
                    Column(Modifier.padding(10.dp)) {
                        Text(item.label, color = AppColors.Slate800, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        Text(item.date, color = AppColors.Slate400, fontSize = 9.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun DataHeader(
    selectMode: Boolean,
    selectedCount: Int,
    visibleCount: Int,
    onDeleteAll: () -> Unit,
    onDeleteSelected: () -> Unit,
    onToggleSelect: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(if (selectMode) AppColors.BlueSoft else Color.White)
            .border(1.dp, AppColors.Border)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.width(72.dp)) {
                when {
                    !selectMode -> DangerSmallButton("전체 삭제", onDeleteAll)
                    selectedCount > 0 -> DangerSmallButton("삭제", onDeleteSelected)
                }
            }
            Text(
                text = if (selectMode) {
                    if (selectedCount > 0) "${selectedCount}개 선택됨" else "항목 선택"
                } else {
                    "수집 데이터"
                },
                modifier = Modifier.weight(1f),
                color = AppColors.Slate800,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            Box(Modifier.width(72.dp), contentAlignment = Alignment.CenterEnd) {
                SmallOutlineButton(if (selectMode) "취소" else "선택", active = selectMode, onClick = onToggleSelect)
            }
        }
        Text(
            text = if (selectMode) "${visibleCount}개 중 선택 · 원하는 항목을 고르세요" else "${visibleCount}개 전체 · 텍스트 0 · 링크 0 · 이미지 0 · 스크린샷 ${visibleCount}",
            color = AppColors.Slate400,
            fontSize = 10.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp),
        )
    }
}

@Composable
private fun ConfirmBanner(text: String, onConfirm: () -> Unit, onCancel: () -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFEF2F2))
            .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.Warning, null, tint = AppColors.Red, modifier = Modifier.size(16.dp))
        Text(text, color = AppColors.Red, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp).weight(1f))
        DangerSmallButton("삭제", onConfirm)
        Spacer(Modifier.width(6.dp))
        SmallOutlineButton("취소", onClick = onCancel)
    }
}

@Composable
private fun PermissionCard(
    permission: PermissionStatus,
    allowedCount: Int,
    onGrantAll: () -> Unit,
    onGrantPartial: () -> Unit,
    onDeny: () -> Unit,
    onReset: () -> Unit,
) {
    Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        when (permission) {
            PermissionStatus.Unknown -> CardBlock {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    IconBubble(Icons.Default.PhotoLibrary, AppColors.Blue, AppColors.BlueSoft, 42)
                    Column(Modifier.weight(1f)) {
                        Text("사진 접근 허용", color = AppColors.Slate800, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("스크린샷을 가져오려면 사진 접근 권한이 필요해요. 데이터는 기기 안에서만 처리됩니다.", color = AppColors.Slate500, fontSize = 11.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GradientButton("모두 허용", modifier = Modifier.weight(1f), onClick = onGrantAll)
                    Button(
                        onClick = onGrantPartial,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.BlueSoft, contentColor = AppColors.Blue),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                    ) { Text("일부 허용", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                }
                TextButton(onClick = onDeny, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("거부", color = AppColors.Slate400, fontSize = 11.sp)
                }
            }
            PermissionStatus.Granted, PermissionStatus.Partial -> CardBlock {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconBubble(Icons.Default.CameraAlt, AppColors.Blue, AppColors.BlueSoft, 30)
                    Spacer(Modifier.width(8.dp))
                    Text("스크린샷 가져오기", color = AppColors.Blue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    if (permission == PermissionStatus.Partial) {
                        Spacer(Modifier.width(6.dp))
                        Pill("일부 허용", Color(0xFFFEF3C7), Color(0xFFD97706))
                    }
                }
                Text(
                    text = if (permission == PermissionStatus.Partial) "${allowedCount}개의 사진에 접근할 수 있어요." else "최근 스크린샷을 다시 스캔하고 이전 항목과 동기화해요.",
                    color = AppColors.Slate500,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GradientButton("다시 스캔", icon = Icons.Default.Refresh, modifier = Modifier.weight(1f), onClick = onReset)
                    Button(
                        onClick = onGrantPartial,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.BlueSoft, contentColor = AppColors.Blue),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                    ) { Text("권한 변경", fontSize = 11.sp) }
                }
            }
            PermissionStatus.Denied -> Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFFAFAFA))
                    .border(1.dp, AppColors.Slate200, RoundedCornerShape(18.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconBubble(Icons.Default.Block, AppColors.Slate400, Color(0xFFF1F5F9), 34)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("사진 접근이 거부됨", color = AppColors.Slate500, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("스크린샷을 가져올 수 없어요.", color = AppColors.Slate400, fontSize = 10.sp)
                }
                SmallOutlineButton("권한 요청", active = true, onClick = onReset)
            }
            PermissionStatus.Selecting -> Unit
        }
    }
}

@Composable
private fun SelectModeBar(selectedCount: Int, onSelectAll: () -> Unit, onClear: () -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFDBEAFE))
            .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(14.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("선택한 데이터 ${selectedCount}개", color = AppColors.Blue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("모두 선택", color = AppColors.Blue, fontSize = 11.sp, modifier = Modifier.clickable(onClick = onSelectAll))
            Row(Modifier.clickable(onClick = onClear), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Replay, null, tint = AppColors.Slate500, modifier = Modifier.size(12.dp))
                Text("초기화", color = AppColors.Slate500, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun FilterRow(filters: List<String>, active: String, screenshotCount: Int, onSelect: (String) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color(0xFFF1F5F9)),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(filters) { filter ->
            val selected = active == filter
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(if (selected) AppColors.Blue else Color(0xFFF1F5F9))
                    .clickable { onSelect(filter) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            ) {
                Text(
                    text = if (filter == "스크린샷") "$filter $screenshotCount" else filter,
                    color = if (selected) Color.White else AppColors.Slate500,
                    fontSize = 10.sp,
                )
            }
        }
    }
}

@Composable
private fun DataItemCard(
    item: ClipboardItem,
    selected: Boolean,
    selectMode: Boolean,
    onToggle: () -> Unit,
    onPreview: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = selectMode, onClick = onToggle),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) AppColors.Blue else AppColors.Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Box {
            Thumbnail(item = item, modifier = Modifier.height(88.dp), showCheck = selectMode, checked = selected)
            IconButtonPlain(
                icon = Icons.Default.CameraAlt,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.Center),
                onClick = onPreview,
            )
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AppColors.Slate900.copy(alpha = 0.55f))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
            ) {
                Text(item.preview, color = Color.White, fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Pill(item.type, Color.White, AppColors.Blue)
                    Spacer(Modifier.width(6.dp))
                    Text(item.mime, color = AppColors.Slate400, fontSize = 9.sp)
                }
                Text(item.name, color = AppColors.Slate800, fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(item.date, color = AppColors.Slate400, fontSize = 9.sp)
            }
            if (!selectMode) {
                DangerSmallButton("삭제", icon = Icons.Default.Delete, onClick = onDelete)
            }
        }
    }
}

@Composable
private fun EmptyDataState(permission: PermissionStatus) {
    val message = when (permission) {
        PermissionStatus.Unknown -> "권한을 허용하면 스크린샷 목록이 표시됩니다."
        PermissionStatus.Denied -> "사진 접근이 거부되었어요."
        else -> "수집한 데이터가 없어요."
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconBubble(if (permission == PermissionStatus.Unknown) Icons.Default.Shield else Icons.Default.CameraAlt, AppColors.Slate400, Color(0xFFF1F5F9), 58)
        Spacer(Modifier.height(12.dp))
        Text(message, color = AppColors.Slate400, fontSize = 13.sp)
        Text("새 항목을 추가해 보세요.", color = Color(0xFFCBD5E1), fontSize = 11.sp)
    }
}

@Composable
private fun PreviewOverlay(item: ClipboardItem, onClose: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(AppColors.Slate900)
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(item.label, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(item.date, color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp)
            }
            IconButtonPlain(Icons.Default.Close, Color.White, onClick = onClose)
        }
        Box(Modifier.fillMaxSize().padding(top = 18.dp), contentAlignment = Alignment.Center) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Brush.linearGradient(listOf(item.color.copy(alpha = 0.25f), item.color.copy(alpha = 0.50f)))),
            ) {
                Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SkeletonLine(item.color, Modifier.width(100.dp).height(14.dp))
                        SkeletonLine(item.color, Modifier.width(64.dp).height(14.dp))
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        repeat(5) { SkeletonLine(item.color, Modifier.fillMaxWidth(if (it % 2 == 0) 1f else 0.75f).height(10.dp)) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SkeletonLine(item.color, Modifier.width(112.dp).height(38.dp))
                        SkeletonLine(item.color, Modifier.width(80.dp).height(38.dp))
                    }
                }
                IconBubble(Icons.Default.CameraAlt, item.color, item.color.copy(alpha = 0.18f), 68, modifier = Modifier.align(Alignment.Center))
                Column(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(AppColors.Slate900.copy(alpha = 0.78f))
                        .padding(16.dp),
                ) {
                    Text(item.preview, color = Color.White, fontSize = 11.sp)
                    Text(item.name, color = Color.White.copy(alpha = 0.5f), fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
private fun TasksScreen(navigate: (Screen, Map<String, String>) -> Unit) {
    val topics = listOf(Topic("1", "Collected items (5)", 5, "5월 26일 11:34 업데이트", AppColors.Blue, listOf("스크린샷", "회의", "여행")))
    val steps = listOf("수집" to true, "클러스터" to true, "주제" to true, "AI 분석" to false, "초안" to false, "확인" to false, "실행" to false)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp),
    ) {
        item {
            SimpleTopHeader("작업", "주제별로 수집한 데이터를 정리하세요")
        }
        item {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                ) {
                    Column(Modifier.background(Brush.linearGradient(listOf(AppColors.Blue, AppColors.BlueDeep))).padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FolderOpen, null, tint = Color.White.copy(alpha = 0.85f), modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("모은 주제", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                        }
                        Text("선택한 데이터를 주제별로 모아두고, 이후 요약/일정/알림 액션을 붙일 수 있습니다.", color = Color(0xFFC7D2FE), fontSize = 11.sp, modifier = Modifier.padding(vertical = 10.dp))
                        Button(
                            onClick = { navigate(Screen.Data, emptyMap()) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.18f), contentColor = Color.White),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                        ) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(15.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("데이터 골라 주제 만들기", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("주제 목록", color = AppColors.Slate800, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("${topics.size}개", color = AppColors.Slate400, fontSize = 10.sp)
                }
                topics.forEach { topic ->
                    TopicRow(topic) { navigate(Screen.TopicDetail, mapOf("topicId" to topic.id)) }
                }
                CardBlock {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountTree, null, tint = AppColors.Blue, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("AI 워크플로우", color = AppColors.Blue, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    FlowStepRow(steps)
                    Text("현재 단계: 주제 생성 완료 · 다음: AI 분석 및 초안 생성", color = AppColors.Slate400, fontSize = 9.sp)
                }
            }
        }
    }
}

@Composable
private fun TopicRow(topic: Topic, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, AppColors.Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            IconBubble(Icons.Default.FolderOpen, topic.color, topic.color.copy(alpha = 0.15f), 46)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(topic.title, color = AppColors.Slate800, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(6.dp))
                    Pill("AI 제안", Color(0xFFDBEAFE), AppColors.BlueDeep)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, null, tint = AppColors.Slate400, modifier = Modifier.size(10.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(topic.lastUpdated, color = AppColors.Slate400, fontSize = 10.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 4.dp)) {
                    topic.tags.forEach { Pill(it, Color.White, AppColors.Blue) }
                }
            }
            Pill(topic.items.toString(), Color.White, AppColors.Blue)
            Icon(Icons.Default.KeyboardArrowRight, null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun TopicDetailScreen(navigate: (Screen, Map<String, String>) -> Unit, data: Map<String, String>) {
    val isNew = data["topicId"] == "new"
    val screenshots = sampleItems.map { it.label to it.color }
    val keyPoints = listOf(
        "회의 자료 1건에서 주간 업무 일정 추가 가능",
        "여행 정보 1건을 제주도 일정 문서로 변환 가능",
        "레시피 1건을 재료 목록 할 일로 변환 가능",
        "행사 안내 1건을 5월 30일 캘린더에 등록 가능",
    )
    val actionCards = listOf(
        Triple("note", "요약 노트", "5개 스크린샷을 구조화된 노트로 요약"),
        Triple("calendar", "캘린더 일정", "워크숍과 제주 여행 일정을 추가"),
        Triple("reminder", "리마인더", "재료 구매와 워크숍 준비 알림"),
        Triple("share", "공유 초안", "정리한 내용을 공유 문서로 작성"),
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeaderRow(
                title = if (isNew) "Screenshot collection" else "Collected items (5)",
                subtitle = "5월 26일 11:34 업데이트 · 5개 항목",
                leading = Icons.Default.ArrowBack,
                onLeading = { navigate(Screen.Tasks, emptyMap()) },
            )
        }
        item {
            Text("관련 데이터 (${screenshots.size}개)", color = AppColors.Slate400, fontSize = 11.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                items(screenshots) { (label, color) ->
                    Column(
                        Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.linearGradient(listOf(color.copy(alpha = 0.14f), color.copy(alpha = 0.28f)))),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(Icons.Default.CameraAlt, null, tint = color, modifier = Modifier.size(20.dp))
                        Text(label, color = color, fontSize = 9.sp)
                    }
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
                Column(Modifier.background(DarkGradient).padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconBubble(Icons.Default.AutoAwesome, Color(0xFF93C5FD), Color(0xFF7DD3FC).copy(alpha = 0.25f), 28)
                        Spacer(Modifier.width(8.dp))
                        Text("AI 분석 요약", color = Color(0xFF93C5FD), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text(
                        "수집한 5개의 스크린샷은 회의 자료, 여행 계획, 레시피, 행사 안내 등 여러 주제를 포함합니다. 각 항목을 분류해 구조화된 노트나 캘린더 일정으로 변환할 수 있어요.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                    Text("핵심 포인트", color = Color(0xFF93C5FD), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    keyPoints.forEach { point ->
                        Row(Modifier.padding(top = 6.dp)) {
                            Box(Modifier.padding(top = 6.dp).size(4.dp).clip(CircleShape).background(Color(0xFF93C5FD)))
                            Text(point, color = Color.White.copy(alpha = 0.72f), fontSize = 11.sp, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
        }
        item {
            CardBlock(background = AppColors.Surface, borderColor = AppColors.Slate200) {
                Text("데이터 소스 참조", color = AppColors.Slate400, fontSize = 10.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 6.dp)) {
                    listOf("Screenshot_Meeting.jpg", "Screenshot_Travel.jpg", "Screenshot_Recipe.jpg", "+2개").forEach {
                        Pill(it, Color.White, AppColors.Blue)
                    }
                }
            }
        }
        item {
            Text("생성된 액션 초안", color = AppColors.Slate800, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                actionCards.forEachIndexed { index, (key, title, description) ->
                    val config = actionConfigs[key] ?: actionConfigs.getValue("note")
                    val clickable = index < 3
                    ActionCardRow(
                        title = title,
                        description = description,
                        config = config,
                        status = if (clickable) "초안" else "닫힘",
                        enabled = clickable,
                    ) {
                        navigate(Screen.ActionReview, mapOf("actionType" to key))
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionCardRow(title: String, description: String, config: ActionConfig, status: String, enabled: Boolean, onClick: () -> Unit) {
    val alpha = if (enabled) 1f else 0.45f
    Card(
        modifier = Modifier.fillMaxWidth().clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = alpha)),
        border = BorderStroke(1.dp, AppColors.Border),
        elevation = CardDefaults.cardElevation(defaultElevation = if (enabled) 1.dp else 0.dp),
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconBubble(config.icon, if (enabled) config.color else Color(0xFFCBD5E1), if (enabled) config.color.copy(alpha = 0.10f) else Color(0xFFF1F5F9), 42)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, color = if (enabled) AppColors.Slate800 else AppColors.Slate400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(6.dp))
                    Pill(status, if (enabled) Color.White else Color(0xFFF1F5F9), if (enabled) AppColors.Blue else AppColors.Slate400)
                }
                Text(description, color = AppColors.Slate400, fontSize = 10.sp)
            }
            if (enabled) Icon(Icons.Default.KeyboardArrowRight, null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun ActionReviewScreen(navigate: (Screen, Map<String, String>) -> Unit, data: Map<String, String>) {
    val config = actionConfigs[data["actionType"]] ?: actionConfigs.getValue("note")
    var isEditing by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(config.defaultTitle) }
    var body by remember { mutableStateOf(config.defaultBody) }
    var executed by remember { mutableStateOf(false) }
    var chatInput by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(listOf(ChatMessage("0", MessageRole.Ai, "초안 내용을 수정하거나 개선하고 싶은 부분이 있으면 말해주세요. 바로 반영해드릴게요.")))
    }
    var isTyping by remember { mutableStateOf(false) }
    var fullscreenChat by remember { mutableStateOf(false) }
    var versions by remember { mutableStateOf(listOf(DraftVersion(1, "v1 원본", config.defaultTitle, config.defaultBody))) }
    var activeVersionId by remember { mutableStateOf(1) }
    var versionsOpen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun saveVersion(newTitle: String, newBody: String) {
        val nextId = versions.size + 1
        versions = versions + DraftVersion(nextId, "v$nextId", newTitle, newBody)
        activeVersionId = nextId
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val userText = text.trim()
        messages = messages + ChatMessage(System.currentTimeMillis().toString(), MessageRole.User, userText)
        chatInput = ""
        isTyping = true
        scope.launch {
            delay(700)
            val reply = when {
                userText.contains("간결") -> {
                    body = "• 회의: Q2 달성률 78%\n• 여행: 제주 3박 4일\n• 레시피: 된장찌개 재료\n• 행사: 워크숍 5/30 14:00\n• 테스트: UI 레이아웃"
                    saveVersion(title, body)
                    "본문을 더 간결하게 줄였어요. 초안에 반영했습니다."
                }
                userText.contains("핵심") -> {
                    body = "주요 일정: 워크숍 5/30, 제주 여행 6/3~6\n구매 필요: 된장찌개 재료\n업무: Q2 목표 달성률 78%"
                    saveVersion(title, body)
                    "핵심 내용만 3줄로 요약했어요."
                }
                userText.contains("제목") -> {
                    title = "5월 4주차 수집 항목 정리"
                    saveVersion(title, body)
                    "제목을 날짜 기반으로 바꿨어요."
                }
                userText.contains("영어") -> {
                    title = "Screenshot Collection Summary Note"
                    body = "Collected Screenshot Analysis\n\n• Meeting: Weekly report, Q2 goal 78%\n• Travel: Jeju Island 3N4D, Aewol stay\n• Recipe: Doenjang-jjigae ingredients\n• Event: Workshop May 30, 2PM\n• Test: UI component layout check"
                    saveVersion(title, body)
                    "제목과 본문을 영어로 번역했어요."
                }
                else -> "요청 내용을 반영했어요. 초안을 확인해주세요."
            }
            isTyping = false
            messages = messages + ChatMessage((System.currentTimeMillis() + 1).toString(), MessageRole.Ai, reply)
        }
    }

    if (fullscreenChat) {
        ChatScreen(
            config = config,
            messages = messages,
            isTyping = isTyping,
            input = chatInput,
            onInput = { chatInput = it },
            onSend = ::sendMessage,
            onBack = { fullscreenChat = false },
        )
        return
    }

    if (executed) {
        LaunchedEffect(Unit) {
            delay(1200)
            navigate(Screen.TopicDetail, mapOf("topicId" to "1"))
        }
        Column(Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            IconBubble(Icons.Default.Check, Color.White, AppColors.Blue, 68)
            Spacer(Modifier.height(16.dp))
            Text("실행 완료!", color = AppColors.Slate800, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("${config.app}로 전달했어요", color = AppColors.Slate400, fontSize = 12.sp)
            Text("잠시 후 이전 화면으로 돌아갑니다.", color = Color(0xFFCBD5E1), fontSize = 11.sp)
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            HeaderRow(
                title = config.title,
                subtitle = "AI 생성 초안 · 사용자 확인 후 실행",
                leading = Icons.Default.ArrowBack,
                leadingTint = AppColors.Slate500,
                onLeading = { navigate(Screen.TopicDetail, mapOf("topicId" to "1")) },
                badgeIcon = config.icon,
                badgeColor = config.color,
            )
        }
        item {
            Row(
                Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFFFBEB))
                    .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(14.dp))
                    .padding(12.dp),
            ) {
                Icon(Icons.Default.Info, null, tint = Color(0xFFD97706), modifier = Modifier.size(15.dp))
                Text(
                    "AI가 작성한 초안입니다. 내용을 확인하고 수정한 뒤 실행해주세요. AI는 사용자 확인 없이 자동 실행하지 않습니다.",
                    color = Color(0xFF92400E),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
        if (versions.size > 1) {
            item {
                Box {
                    Button(
                        onClick = { versionsOpen = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFAFBFF), contentColor = config.color),
                        border = BorderStroke(1.dp, config.color.copy(alpha = 0.20f)),
                    ) {
                        Icon(Icons.Default.Replay, null, modifier = Modifier.size(13.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("편집 버전", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.width(8.dp))
                        Pill(versions.find { it.id == activeVersionId }?.label.orEmpty(), config.color.copy(alpha = 0.10f), config.color)
                        Icon(Icons.Default.KeyboardArrowDown, null, modifier = Modifier.size(16.dp))
                    }
                    DropdownMenu(expanded = versionsOpen, onDismissRequest = { versionsOpen = false }) {
                        versions.forEach { version ->
                            DropdownMenuItem(
                                text = { Text(version.label) },
                                onClick = {
                                    title = version.title
                                    body = version.body
                                    activeVersionId = version.id
                                    versionsOpen = false
                                },
                            )
                        }
                    }
                }
            }
        }
        item {
            DraftCard(
                title = title,
                body = body,
                config = config,
                isEditing = isEditing,
                onTitle = { title = it },
                onBody = { body = it },
            )
        }
        item {
            ChatPreview(
                config = config,
                messages = messages,
                isTyping = isTyping,
                input = chatInput,
                onInput = { chatInput = it },
                onSend = ::sendMessage,
                onOpenFull = { fullscreenChat = true },
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (isEditing) saveVersion(title, body)
                        isEditing = !isEditing
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isEditing) config.color.copy(alpha = 0.10f) else Color.White, contentColor = config.color),
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(if (isEditing) "완료" else "수정", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { executed = true },
                    enabled = !isEditing,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isEditing) Color(0xFFCBD5E1) else config.color, contentColor = Color.White),
                ) {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("실행", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun DraftCard(
    title: String,
    body: String,
    config: ActionConfig,
    isEditing: Boolean,
    onTitle: (String) -> Unit,
    onBody: (String) -> Unit,
) {
    CardBlock {
        FieldBlock("제목") {
            if (isEditing) {
                OutlinedTextField(value = title, onValueChange = onTitle, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
            } else {
                ReadOnlyBox { Text(title, color = AppColors.Slate800, fontSize = 12.sp, fontWeight = FontWeight.Medium) }
            }
        }
        FieldBlock("본문") {
            if (isEditing) {
                OutlinedTextField(value = body, onValueChange = onBody, minLines = 7, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp))
            } else {
                ReadOnlyBox { Text(body, color = AppColors.Slate800, fontSize = 11.sp, lineHeight = 17.sp) }
            }
        }
        FieldBlock("전달 앱") {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(config.appBg)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Smartphone, null, tint = config.color, modifier = Modifier.size(15.dp))
                Text(config.app, color = AppColors.Slate800, fontSize = 12.sp, modifier = Modifier.padding(start = 8.dp).weight(1f))
                Pill("Samsung", config.color.copy(alpha = 0.10f), config.color)
            }
        }
        FieldBlock("관련 데이터 소스") {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("스크린샷 × 5", "Collected items (5)", "5월 20~26일").forEach {
                    Pill(it, Color.White, AppColors.Blue)
                }
            }
        }
    }
}

@Composable
private fun ChatPreview(
    config: ActionConfig,
    messages: List<ChatMessage>,
    isTyping: Boolean,
    input: String,
    onInput: (String) -> Unit,
    onSend: (String) -> Unit,
    onOpenFull: () -> Unit,
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.5.dp, config.color.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
            .background(Color.White),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenFull)
                .background(Brush.linearGradient(listOf(config.color.copy(alpha = 0.13f), config.color.copy(alpha = 0.04f))))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconBubble(Icons.Default.AutoAwesome, Color.White, config.color, 28)
            Text("AI에게 수정 요청", color = config.color, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            Text("초안에 즉시 반영", color = config.color.copy(alpha = 0.65f), fontSize = 9.sp, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.End)
        }
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            messages.takeLast(3).forEach { ChatBubble(it, config.color, compact = true) }
            if (isTyping) TypingBubble(config.color, compact = true)
        }
        QuickSuggestionRow(config.color, onSend)
        ChatInputRow(color = config.color, input = input, onInput = onInput, onSend = onSend)
    }
}

@Composable
private fun ChatScreen(
    config: ActionConfig,
    messages: List<ChatMessage>,
    isTyping: Boolean,
    input: String,
    onInput: (String) -> Unit,
    onSend: (String) -> Unit,
    onBack: () -> Unit,
) {
    Column(Modifier.fillMaxSize().background(Color.White).imePadding()) {
        HeaderRow(
            title = "AI 수정 요청",
            subtitle = "${config.title} · 초안에 즉시 반영",
            leading = Icons.Default.ArrowBack,
            onLeading = onBack,
            badgeIcon = Icons.Default.AutoAwesome,
            badgeColor = config.color,
            modifier = Modifier.background(Brush.linearGradient(listOf(config.color.copy(alpha = 0.10f), config.color.copy(alpha = 0.04f)))),
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(messages) { ChatBubble(it, config.color) }
            if (isTyping) item { TypingBubble(config.color) }
        }
        QuickSuggestionRow(config.color, onSend)
        ChatInputRow(color = config.color, input = input, onInput = onInput, onSend = onSend)
    }
}

@Composable
private fun ChatBubble(message: ChatMessage, color: Color, compact: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.role == MessageRole.User) Arrangement.End else Arrangement.Start,
    ) {
        if (message.role == MessageRole.Ai) {
            IconBubble(Icons.Default.AutoAwesome, Color.White, color, if (compact) 22 else 26)
            Spacer(Modifier.width(8.dp))
        }
        Box(
            Modifier
                .fillMaxWidth(if (compact) 0.80f else 0.78f)
                .clip(RoundedCornerShape(18.dp))
                .background(if (message.role == MessageRole.Ai) Color(0xFFF1F5F9) else color)
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Text(message.text, color = if (message.role == MessageRole.Ai) AppColors.Slate800 else Color.White, fontSize = if (compact) 11.sp else 12.sp, lineHeight = 17.sp)
        }
    }
}

@Composable
private fun TypingBubble(color: Color, compact: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconBubble(Icons.Default.AutoAwesome, Color.White, color, if (compact) 22 else 26)
        Spacer(Modifier.width(8.dp))
        Row(
            Modifier
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFF1F5F9))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            repeat(3) { index ->
                val alpha by animateFloatAsState(targetValue = if (index % 2 == 0) 0.4f else 1f, label = "typing")
                Box(Modifier.size(6.dp).clip(CircleShape).background(AppColors.Slate400.copy(alpha = alpha)))
            }
        }
    }
}

@Composable
private fun QuickSuggestionRow(color: Color, onSend: (String) -> Unit) {
    LazyRow(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.border(1.dp, Color(0xFFF1F5F9)),
    ) {
        items(listOf("더 간결하게", "핵심만 요약", "제목 바꿔줘", "영어로 번역")) { suggestion ->
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.08f))
                    .clickable { onSend(suggestion) }
                    .padding(horizontal = 12.dp, vertical = 7.dp),
            ) {
                Text(suggestion, color = color, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun ChatInputRow(color: Color, input: String, onInput: (String) -> Unit, onSend: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFFFAFAFA))
            .padding(12.dp)
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = input,
            onValueChange = onInput,
            modifier = Modifier.weight(1f),
            placeholder = { Text("수정 내용을 입력하세요...", color = AppColors.Slate400, fontSize = 12.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSend(input) }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )
        Button(
            onClick = { onSend(input) },
            enabled = input.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            modifier = Modifier.size(38.dp),
            colors = ButtonDefaults.buttonColors(containerColor = color, disabledContainerColor = AppColors.Slate200),
        ) {
            Icon(Icons.Default.Send, null, tint = if (input.isNotBlank()) Color.White else AppColors.Slate400, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun SimpleTopHeader(title: String, subtitle: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, AppColors.Border)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title, color = AppColors.Slate800, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = AppColors.Slate400, fontSize = 10.sp)
    }
}

@Composable
private fun HeaderRow(
    title: String,
    subtitle: String,
    leading: ImageVector,
    onLeading: () -> Unit,
    modifier: Modifier = Modifier,
    leadingTint: Color = AppColors.Slate500,
    badgeIcon: ImageVector? = null,
    badgeColor: Color = AppColors.Blue,
    action: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, AppColors.Border)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButtonPlain(leading, leadingTint, bg = Color(0xFFF1F5F9), onClick = onLeading)
        Spacer(Modifier.width(12.dp))
        badgeIcon?.let {
            IconBubble(it, badgeColor, badgeColor.copy(alpha = 0.10f), 30)
            Spacer(Modifier.width(8.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(title, color = AppColors.Slate800, fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(subtitle, color = AppColors.Slate400, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        action?.invoke()
    }
}

@Composable
private fun CardBlock(
    modifier: Modifier = Modifier,
    background: Color = Color.White,
    borderColor: Color = AppColors.Border,
    content: @Composable Column.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(Modifier.padding(14.dp), content = content)
    }
}

@Composable
private fun GradientButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    compact: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(if (compact) 10.dp else 16.dp)
    Box(
        modifier
            .clip(shape)
            .background(if (enabled) BlueGradient else SolidColor(AppColors.Slate200))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = if (compact) 12.dp else 16.dp, vertical = if (compact) 7.dp else 13.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            icon?.let {
                Icon(it, null, tint = Color.White, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
            }
            Text(text, color = Color.White, fontSize = if (compact) 11.sp else 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DangerSmallButton(text: String, icon: ImageVector? = null, onClick: () -> Unit) {
    Row(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFEF2F2))
            .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let {
            Icon(it, null, tint = AppColors.Red, modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(4.dp))
        }
        Text(text, color = AppColors.Red, fontSize = 10.sp)
    }
}

@Composable
private fun SmallOutlineButton(text: String, active: Boolean = false, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (active) AppColors.Blue else Color.White)
            .border(1.dp, if (active) AppColors.Blue else AppColors.Slate200, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    ) {
        Text(text, color = if (active) Color.White else AppColors.Slate500, fontSize = 10.sp)
    }
}

@Composable
private fun Pill(text: String, bg: Color, color: Color) {
    Box(Modifier.clip(CircleShape).background(bg).padding(horizontal = 7.dp, vertical = 3.dp)) {
        Text(text, color = color, fontSize = 9.sp, maxLines = 1)
    }
}

@Composable
private fun StatChip(icon: ImageVector, label: String, color: Color) {
    Row(
        Modifier
            .clip(CircleShape)
            .background(if (color == AppColors.Blue) Color.White else AppColors.Surface)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(10.dp))
        Spacer(Modifier.width(4.dp))
        Text(label, color = color, fontSize = 10.sp)
    }
}

@Composable
private fun IconBubble(icon: ImageVector, tint: Color, bg: Color, size: Int, modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size / 4).dp))
            .background(bg),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size((size * 0.45f).dp))
    }
}

@Composable
private fun IconButtonPlain(
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    bg: Color = Color.Transparent,
    onClick: () -> Unit,
) {
    Box(
        modifier
            .size(34.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(17.dp))
    }
}

@Composable
private fun Thumbnail(item: ClipboardItem, modifier: Modifier = Modifier, showCheck: Boolean = false, checked: Boolean = false) {
    Box(
        modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(item.color.copy(alpha = 0.10f), item.color.copy(alpha = 0.24f)))),
    ) {
        Column(Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SkeletonLine(item.color, Modifier.width(64.dp).height(8.dp))
                SkeletonLine(item.color, Modifier.width(42.dp).height(8.dp))
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                SkeletonLine(item.color, Modifier.fillMaxWidth().height(6.dp))
                SkeletonLine(item.color, Modifier.fillMaxWidth(0.76f).height(6.dp))
                SkeletonLine(item.color, Modifier.fillMaxWidth(0.84f).height(6.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SkeletonLine(item.color, Modifier.width(64.dp).height(20.dp))
                SkeletonLine(item.color, Modifier.width(48.dp).height(20.dp))
            }
        }
        Icon(Icons.Default.CameraAlt, null, tint = item.color.copy(alpha = 0.40f), modifier = Modifier.align(Alignment.Center).size(26.dp))
        if (showCheck) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (checked) AppColors.Blue else Color.White.copy(alpha = 0.90f))
                    .border(2.dp, if (checked) AppColors.Blue else Color(0xFFCBD5E1), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                if (checked) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
            }
        }
    }
}

@Composable
private fun SkeletonLine(color: Color, modifier: Modifier) {
    Box(modifier.clip(RoundedCornerShape(5.dp)).background(color.copy(alpha = 0.36f)))
}

@Composable
private fun DividerLine(modifier: Modifier = Modifier) {
    Box(modifier.height(1.dp).background(AppColors.Slate200))
}

@Composable
private fun FlowStepRow(steps: List<Pair<String, Boolean>>) {
    Row(
        Modifier
            .padding(vertical = 10.dp)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        steps.forEachIndexed { index, step ->
            Pill(step.first, if (step.second) AppColors.Blue else Color.White, if (step.second) Color.White else AppColors.Slate400)
            if (index < steps.lastIndex) {
                Text("→", color = Color(0xFFCBD5E1), fontSize = 10.sp, modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
    }
}

@Composable
private fun FieldBlock(label: String, content: @Composable () -> Unit) {
    Column(Modifier.padding(bottom = 12.dp)) {
        Text(label, color = AppColors.Slate400, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(5.dp))
        content()
    }
}

@Composable
private fun ReadOnlyBox(content: @Composable () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(AppColors.Surface)
            .padding(12.dp),
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun SmartClipboardPreview() {
    SmartClipboardAITheme {
        SmartClipboardAIApp()
    }
}
