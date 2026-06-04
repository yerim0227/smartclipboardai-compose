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
fun ActionReviewScreen(navigate: (Screen, Map<String, String>) -> Unit, data: Map<String, String>) {
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
fun DraftCard(
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
fun ChatPreview(
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
fun ChatScreen(
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
fun ChatBubble(message: ChatMessage, color: Color, compact: Boolean = false) {
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
fun TypingBubble(color: Color, compact: Boolean = false) {
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
fun QuickSuggestionRow(color: Color, onSend: (String) -> Unit) {
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
fun ChatInputRow(color: Color, input: String, onInput: (String) -> Unit, onSend: (String) -> Unit) {
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
