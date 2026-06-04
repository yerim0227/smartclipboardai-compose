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
fun HomeScreen(navigate: (Screen, Map<String, String>) -> Unit) {
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
fun DashboardSections(
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
