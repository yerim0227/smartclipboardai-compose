package com.smartclipboardai.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class HistoryDraft(
    val id: String,
    val type: String,
    val icon: ImageVector,
    val color: Color,
    val description: String,
    val status: String,
)

private data class HistoryTopic(
    val id: String,
    val title: String,
    val date: String,
    val dataCount: Int,
    val summary: String,
    val drafts: List<HistoryDraft>,
)

private val historyTopics = listOf(
    HistoryTopic(
        id = "1",
        title = "Screenshot collection",
        date = "May 26, 11:34",
        dataCount = 5,
        summary = "Meeting materials, travel plans, recipes, and event notes were grouped together.",
        drafts = listOf(
            HistoryDraft("note", "Daily note", Icons.Default.Description, AppColors.Blue, "Organized five screenshots into a daily note.", "Executed"),
            HistoryDraft("calendar", "Calendar", Icons.Default.CalendarMonth, Color(0xFF2563EB), "Added workshop and travel schedules.", "Executed"),
            HistoryDraft("reminder", "Reminder", Icons.Default.Notifications, AppColors.BlueDeep, "Prepared shopping and workshop reminders.", "Dismissed"),
            HistoryDraft("share", "Share", Icons.Default.Share, AppColors.Cyan, "Prepared a message summary.", "Draft"),
        ),
    ),
    HistoryTopic(
        id = "2",
        title = "Jeju travel plan",
        date = "May 22, 09:18",
        dataCount = 3,
        summary = "Travel schedule, stay information, and saved places.",
        drafts = listOf(
            HistoryDraft("note", "Daily note", Icons.Default.Description, AppColors.Blue, "Created a trip summary.", "Executed"),
            HistoryDraft("calendar", "Calendar", Icons.Default.CalendarMonth, Color(0xFF2563EB), "Registered the travel dates.", "Executed"),
            HistoryDraft("share", "Share", Icons.Default.Share, AppColors.Cyan, "Made a travel message draft.", "Edited"),
        ),
    ),
    HistoryTopic(
        id = "3",
        title = "Weekly meeting notes",
        date = "May 19, 14:55",
        dataCount = 4,
        summary = "Meeting notes, next steps, and upcoming schedules.",
        drafts = listOf(
            HistoryDraft("note", "Daily note", Icons.Default.Description, AppColors.Blue, "Summarized meeting points.", "Executed"),
            HistoryDraft("reminder", "Reminder", Icons.Default.Notifications, AppColors.BlueDeep, "Created follow-up reminders.", "Executed"),
        ),
    ),
)

@Composable
fun HistoryScreen(navigate: (Screen, Map<String, String>) -> Unit) {
    var expandedId by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Surface),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = { navigate(Screen.Home, emptyMap()) },
                    modifier = Modifier.size(36.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9), contentColor = AppColors.Slate500),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                ) {
                    Icon(Icons.Default.ArrowBack, null, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("History", color = AppColors.Slate800, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Previous AI organization jobs ${historyTopics.size}", color = AppColors.Slate400, fontSize = 10.sp)
                }
            }
        }
        items(historyTopics) { topic ->
            val open = expandedId == topic.id
            HistoryTopicCard(
                topic = topic,
                open = open,
                onToggle = { expandedId = if (open) null else topic.id },
                onDetail = { navigate(Screen.TopicDetail, mapOf("topicId" to topic.id, "topicTitle" to topic.title, "from" to "history")) },
                onDraft = { actionType -> navigate(Screen.ActionReview, mapOf("actionType" to actionType, "topicId" to topic.id, "topicTitle" to topic.title, "from" to "history")) },
            )
        }
    }
}

@Composable
private fun HistoryTopicCard(
    topic: HistoryTopic,
    open: Boolean,
    onToggle: () -> Unit,
    onDetail: () -> Unit,
    onDraft: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, AppColors.Border),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onToggle)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.size(42.dp).background(AppColors.BlueSoft, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.CameraAlt, null, tint = AppColors.Blue, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(topic.title, color = AppColors.Slate800, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${topic.date} · ${topic.dataCount} data · ${topic.drafts.size} drafts", color = AppColors.Slate400, fontSize = 10.sp)
                }
                val executedCount = topic.drafts.count { it.status == "Executed" }
                if (executedCount > 0) {
                    Pill("$executedCount executed", Color(0xFFD1FAE5), AppColors.Green)
                }
                Icon(Icons.Default.KeyboardArrowRight, null, tint = AppColors.Slate200, modifier = Modifier.size(16.dp))
            }

            if (open) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .background(Color(0xFFF0F5FF), RoundedCornerShape(14.dp))
                        .padding(12.dp),
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = AppColors.Blue, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(topic.summary, color = AppColors.BlueDeep, fontSize = 11.sp, modifier = Modifier.weight(1f))
                }
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    topic.drafts.forEach { draft ->
                        val clickable = draft.status == "Draft" || draft.status == "Edited"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFAFBFF), RoundedCornerShape(14.dp))
                                .clickable(enabled = clickable) { onDraft(draft.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(Modifier.size(34.dp).background(draft.color.copy(alpha = 0.10f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                Icon(draft.icon, null, tint = draft.color, modifier = Modifier.size(15.dp))
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(draft.type, color = AppColors.Slate800, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.width(6.dp))
                                    StatusPill(draft.status)
                                }
                                Text(draft.description, color = AppColors.Slate400, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                            if (clickable) Icon(Icons.Default.KeyboardArrowRight, null, tint = AppColors.Slate200, modifier = Modifier.size(14.dp))
                        }
                    }
                    Button(
                        onClick = onDetail,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AppColors.BlueSoft, contentColor = AppColors.Blue),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                    ) {
                        Text("View full detail", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusPill(status: String) {
    val pair = when (status) {
        "Executed" -> Color(0xFFD1FAE5) to AppColors.Green
        "Edited" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "Dismissed" -> Color(0xFFF1F5F9) to AppColors.Slate400
        else -> AppColors.BlueSoft to AppColors.Blue
    }
    Pill(status, pair.first, pair.second)
}
