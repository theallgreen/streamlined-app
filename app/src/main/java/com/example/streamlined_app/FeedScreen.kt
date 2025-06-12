package com.example.streamlined_app


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.text.style.TextOverflow


@Preview(showBackground = true)
@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    query: String = "testest",
    sources: List<Int> = listOf(1, 1, 1, 1)
) {
    val posts = listOf(
        FeedPost(
            source = Source("Youtube", "Y", Color.Red),
            user = "Oklahoma City Thunder vs Indiana Pacers",
            badge = "from Youtube",
            text = "",
            imageUrl = "https://i.imgur.com/YOUR_DUMMY_THUMBNAIL.jpg", // Put a correct sample URL or skip the AsyncImage if not using Coil
            tags = listOf("#gametimehighlights", "#nba", "#nbahighlights"),
            stats = "1.1M views, 11K likes, 725 comments"
        ),
        FeedPost(
            source = Source("X/Twitter", "X", Color.Black),
            user = "NBA",
            badge = "from Twitter/X",
            text = "Shai follows 38 in Game 1 with 34 tonight\nMOST POINTS EVER by a player in his first 2 career Finals games",
            imageUrl = null,
            tags = emptyList(),
            stats = "11K likes    480 comments"
        ),
        FeedPost(
            source = Source("Reddit", "R", Color(0xFFFF5722)),
            user = "Moderator17",
            badge = "from Reddit",
            text = "[Post Game Thread] The Oklahoma City Thunder control Game 2 against the Indiana Pacers, winning 123-107, behind SGA's 34 pts as they even the series 1-1",
            imageUrl = null,
            tags = emptyList(),
            stats = "5.8K upvotes    1.5K comments"
        ),
    )

    Column(modifier
        .fillMaxSize()
        .background(Color.White)
    ) {
        // TopBar
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp, start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                "Back to Home",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.width(40.dp)) // To balance back button
        }

        // Searchbar (simple, not functional for demo)
        OutlinedTextField(
            value = "NBA finals game 2",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(44.dp),
            readOnly = true,
            singleLine = true,
            trailingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search")
            },
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(10.dp))

        // Feed List
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
        ) {
            items(posts) { post ->
                FeedPostCard(post)
            }
        }
    }
}

@Composable
fun LazyColumn(contentPadding: PaddingValues, content: () -> Unit) {

}

data class FeedPost(
    val source: Source,
    val user: String,
    val badge: String,
    val text: String,
    val imageUrl: String?,
    val tags: List<String>,
    val stats: String,
)

@Composable
fun FeedPostCard(post: FeedPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SourceIcon(source = post.source)
                Column(Modifier.padding(start = 8.dp)) {
                    Text(
                        post.user,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        post.badge,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
                Spacer(Modifier.weight(1f))
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
            }

            if (post.imageUrl != null) {
                // For image loading, you can use Coil, or comment out this part if not added yet
                Spacer(modifier = Modifier.height(8.dp))
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    // Dummy Box in place of actual image loader:
                    Box(
                        Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Thumbnail", color = Color.DarkGray)
                    }
                }
            }

            if (post.tags.isNotEmpty()) {
                Row(Modifier.padding(top = 6.dp)) {
                    post.tags.forEach {
                        Text(
                            it,
                            color = Color(0xFF0356E7),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            maxLines = 1,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                }
            }

            if (post.text.isNotEmpty()) {
                Text(
                    post.text,
                    Modifier.padding(top = 8.dp),
                    fontSize = 15.sp
                )
            }
            Text(
                post.stats,
                Modifier.padding(top = 10.dp),
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}