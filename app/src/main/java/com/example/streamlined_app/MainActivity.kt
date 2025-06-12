package com.example.streamlined_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.streamlined_app.ui.theme.StreamlinedappTheme
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import android.content.Context
import androidx.compose.ui.platform.LocalContext


fun saveRecentSearch(context: Context, query: String) {
    val prefs = context.getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
    val raw = prefs.getString("recent_searches_list", "") ?: ""
    val list = raw.split("|||")
        .filter { it.isNotBlank() && it != query }
        .toMutableList()
    list.add(query)
    while (list.size > 5) list.removeAt(0)
    prefs.edit().putString("recent_searches_list", list.joinToString("|||")).apply()
}

fun getRecentSearches(context: Context): List<String> {
    val prefs = context.getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
    return prefs.getString("recent_searches_list", "")
        ?.split("|||")
        ?.filter { it.isNotBlank() }
        ?.takeLast(5)
        ?.reversed()
        ?: listOf()
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            var feed by remember { mutableStateOf(false) }
            var query by remember { mutableStateOf("") }
            var sources by remember { mutableStateOf(listOf(1, 1, 1, 1)) }

            val context = LocalContext.current
            var recentSearches by remember { mutableStateOf(getRecentSearches(context)) }


            LaunchedEffect(feed) {
                if (!feed) {
                    recentSearches = getRecentSearches(context)
                }
            }


            StreamlinedappTheme {
                if (!feed) {
                    HomeScreen(

                        recentSearches = recentSearches,

                        onSettingsClick = { /* TODO */ },


                        onRecentSearchClick = {q: String, s:List<Int> ->
                            saveRecentSearch(context, q)
                            recentSearches = getRecentSearches(context)
                            query = q
                            sources = s
                            feed = true
                        },
                        onSearch = {q: String, s:List<Int> ->
                            saveRecentSearch(context, q)
                            recentSearches = getRecentSearches(context)
                            query = q
                            sources = s
                            feed = true
                        }
                    )
                } else {
                    FeedScreen(
                        onBackClick = { feed = false },
                        query = query,
                        sources = sources,
                        onSearch = { newQuery, newSources ->
                            saveRecentSearch(context, newQuery)
                            recentSearches = getRecentSearches(context)
                            query = newQuery
                            sources = newSources

                        }
                    )
                }
            }
        }
    }
}

