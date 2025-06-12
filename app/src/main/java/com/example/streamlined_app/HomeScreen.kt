package com.example.streamlined_app


import android.content.Context
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
import kotlinx.coroutines.launch


@Preview(showBackground = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {},
    onRecentSearchClick: (String, List<Int>) -> Unit = { _, _ -> },
    onSearch: (String, List<Int>) -> Unit,
    recentSearches: List<String>,


    ) {

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val sources = listOf(
        Source("Youtube", "Y", Color.Red),
        Source("X/Twitter", "X", Color.Black),
        Source("Reddit", "R", Color(0xFFFF5722)),
        Source("Twitch", "T", Color(0xFF9147FF))
    )
    var searchText by remember { mutableStateOf("") }
    val checkedState = remember {
        mutableStateMapOf<String, Boolean>().apply {
            sources.forEach { this[it.name] = true }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(Color.White)
        ) {
            // Top AppBar
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Streamlined",
                    Modifier.weight(3f),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = onSettingsClick, modifier = Modifier.weight(1f)) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            // Search bar & sources
            Card(
                Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(12.dp))
            ) {
                Column(Modifier.padding(12.dp)) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF4F4F4), RoundedCornerShape(8.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("What's on your mind?") },
                            colors = TextFieldDefaults.colors(
//                            containerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            maxLines = 1
                        )
                        IconButton(onClick = {
                            val selectedSources =
                                sources.map { if (checkedState[it.name] == true) 1 else 0 }
                            if (searchText.isBlank()) {
                                coroutineScope.launch { snackbarHostState.showSnackbar("Enter a search term") }
                            } else if (!(selectedSources.size == 4 && selectedSources[2] == 1 && selectedSources.count { it == 1 } == 1)) {
                                coroutineScope.launch { snackbarHostState.showSnackbar("Only reddit searching is supported at this time.") }
                            } else {
                                onSearch(searchText, selectedSources)
                            }
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }

                    // Source list
                    Column(
                        modifier = Modifier.padding(top = 14.dp)
                    ) {
                        sources.forEach { source ->
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SourceIcon(source)
                                Text(
                                    source.name,
                                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                                    fontSize = 16.sp
                                )
                                Checkbox(
                                    checked = checkedState[source.name] == true,
                                    onCheckedChange = { checkedState[source.name] = it }
                                )
                            }
                            if (source != sources.last()) Divider(
                                modifier = Modifier.padding(
                                    vertical = 2.dp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            if (recentSearches.isNotEmpty()) {
                Text(
                    "Recent Searches:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Column {
                    recentSearches.forEach { term ->
                        Button(
                            onClick = {
                                val selectedSources =
                                    sources.map { if (checkedState[it.name] == true) 1 else 0 }
                                onRecentSearchClick(term, selectedSources)
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text(term, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}



data class Source(val name: String, val symbol: String, val color: Color)
@Composable
fun SourceIcon(source: Source) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(source.color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(source.symbol, color = Color.White, fontWeight = FontWeight.Bold)
    }
}