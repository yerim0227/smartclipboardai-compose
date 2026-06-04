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

enum class Screen { Home, Data, Tasks, TopicDetail, ActionReview, Storage, History, AiSuggest, Analyzing }
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

val sampleItems = listOf(
    ClipboardItem("1", "Screenshot_20260526_091234_Meeting.jpg", "스크린샷", "image/jpeg", "5월 26일 09:12", AppColors.Blue, "회의 자료", "주간 업무 보고, Q2 목표 달성률 78%, 대비 현황 요약"),
    ClipboardItem("2", "Screenshot_20260524_133021_Travel.jpg", "스크린샷", "image/jpeg", "5월 24일 13:30", AppColors.BlueDeep, "여행 계획", "제주도 3박 4일, 애월 숙소, 렌터카 예약 완료"),
    ClipboardItem("3", "Screenshot_20260523_080045_Recipe.jpg", "스크린샷", "image/jpeg", "5월 23일 08:00", AppColors.Cyan, "레시피", "된장찌개 재료: 된장, 두부, 호박, 대파"),
    ClipboardItem("4", "Screenshot_20260522_195532_Event.jpg", "스크린샷", "image/jpeg", "5월 22일 19:55", AppColors.Green, "행사 안내", "사내 워크숍 안내, 5월 30일 오후 2시, 본사 B동 3층"),
    ClipboardItem("5", "Screenshot_20260520_164512_Test.jpg", "스크린샷", "image/jpeg", "5월 20일 16:45", AppColors.Blue, "테스트", "UI 컴포넌트 레이아웃 테스트, 버튼 정렬과 여백 확인"),
)

val actionConfigs = mapOf(
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
