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

@Composable
fun TopicDetailScreen(navigate: (Screen, Map<String, String>) -> Unit, data: Map<String, String>) {
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
fun ActionCardRow(title: String, description: String, config: ActionConfig, status: String, enabled: Boolean, onClick: () -> Unit) {
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
