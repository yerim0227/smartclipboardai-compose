package com.smartclipboardai.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class SuggestedTopic(
    val id: String,
    val title: String,
    val description: String,
    val dataTypes: List<String>,
    val tags: List<String>,
    val icon: ImageVector,
    val color: Color,
    val accentBg: Color,
)

val suggestedTopics = listOf(
    SuggestedTopic("1", "Meeting materials", "Turn screenshots and notes into drafts, events, and reminders.", listOf("3 screenshots", "2 notes"), listOf("work", "meeting"), Icons.Default.Description, AppColors.Blue, AppColors.BlueSoft),
    SuggestedTopic("2", "Jeju travel plan", "Collect stays, places, and links into a travel guide.", listOf("4 screenshots", "3 links"), listOf("travel", "schedule"), Icons.Default.Flight, AppColors.Cyan, Color(0xFFECFEFF)),
    SuggestedTopic("3", "Recipe collection", "Extract ingredients and steps from saved images.", listOf("6 screenshots"), listOf("recipe", "food"), Icons.Default.CameraAlt, AppColors.Green, Color(0xFFECFDF5)),
    SuggestedTopic("4", "Shopping wishlist", "Compare saved product links and captures.", listOf("2 screenshots", "6 links"), listOf("shopping"), Icons.Default.ShoppingCart, Color(0xFFD97706), Color(0xFFFFFBEB)),
    SuggestedTopic("5", "Dev references", "Group code screenshots and docs by topic.", listOf("5 screenshots", "4 links"), listOf("dev", "code"), Icons.Default.Code, Color(0xFF7C3AED), Color(0xFFF5F3FF)),
)

@Composable
fun AiSuggestScreen(navigate: (Screen, Map<String, String>) -> Unit, data: Map<String, String>) {
    val skipLoading = data["skipLoading"] == "true"
    val query = data["query"].orEmpty()
    var loading by remember { mutableStateOf(!skipLoading) }

    LaunchedEffect(skipLoading) {
        if (!skipLoading) {
            delay(1600)
            loading = false
        }
    }

    if (loading) {
        AiSuggestLoading(query = query) { navigate(Screen.Home, emptyMap()) }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Surface),
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGradient)
                    .padding(16.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { navigate(Screen.Home, emptyMap()) },
                        modifier = Modifier.size(36.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.12f)),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                    ) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF93C5FD), modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("AI suggested topics", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(18.dp))
                        .padding(14.dp),
                ) {
                    Text("Analysis result", color = Color(0xFF93C5FD), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(
                        if (query.isBlank()) "Found ${suggestedTopics.size} topics from all collected data."
                        else "Found ${suggestedTopics.size} topics for \"$query\".",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text("Pick one and AI will prepare action drafts.", color = Color(0xFFA5B4FC), fontSize = 10.sp)
                }
            }
        }
        itemsIndexed(suggestedTopics) { _, topic ->
            SuggestedTopicCard(topic = topic) {
                navigate(
                    Screen.TopicDetail,
                    mapOf(
                        "topicId" to topic.id,
                        "topicTitle" to topic.title,
                        "from" to "aiSuggest",
                        "query" to query,
                    ),
                )
            }
        }
        item {
            Button(
                onClick = { navigate(Screen.Data, emptyMap()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = AppColors.Slate500),
                border = BorderStroke(1.dp, AppColors.Slate200),
            ) {
                Text("Pick data directly", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AiSuggestLoading(query: String, onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGradient)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(Modifier.fillMaxWidth()) {
            Button(
                onClick = onClose,
                modifier = Modifier.size(38.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.12f)),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            ) {
                Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
        Spacer(Modifier.height(132.dp))
        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
                .border(2.dp, Color(0xFF93C5FD).copy(alpha = 0.45f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF93C5FD), modifier = Modifier.size(30.dp))
        }
        Spacer(Modifier.height(28.dp))
        Text("AI is analyzing", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
        if (query.isNotBlank()) {
            Text("\"$query\"", color = Color(0xFF93C5FD), fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp))
        }
        Spacer(Modifier.height(28.dp))
        listOf("Scanning collected data", "Classifying patterns", "Preparing suggested topics").forEachIndexed { index, label ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp),
            ) {
                Icon(
                    imageVector = if (index == 0) Icons.Default.Check else Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = if (index == 0) Color(0xFF34D399) else Color(0xFF93C5FD),
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(10.dp))
                Text(label, color = Color.White.copy(alpha = 0.86f), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SuggestedTopicCard(topic: SuggestedTopic, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, AppColors.Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(topic.accentBg, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(topic.icon, null, tint = topic.color, modifier = Modifier.size(20.dp))
                }
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(topic.title, color = AppColors.Slate800, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.KeyboardArrowRight, null, tint = AppColors.Slate200, modifier = Modifier.size(16.dp))
                    }
                    Text(topic.description, color = AppColors.Slate500, fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAFBFF))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    topic.dataTypes.forEach { Pill(it, topic.accentBg, topic.color) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    topic.tags.take(2).forEach { Text("#$it", color = AppColors.Slate400, fontSize = 9.sp) }
                }
            }
        }
    }
}
