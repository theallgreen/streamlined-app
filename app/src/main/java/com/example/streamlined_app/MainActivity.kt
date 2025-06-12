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
    val set = LinkedHashSet(prefs.getStringSet("searches", emptySet()))
    // Remove then re-add to keep "most recent" at end
    set.remove(query)
    set.add(query)
    // Keep a maximum of 10
    while (set.size > 10) set.remove(set.first())
    prefs.edit().putStringSet("searches", set).apply()
}

fun getRecentSearches(context: Context): List<String> {
    val prefs = context.getSharedPreferences("recent_searches", Context.MODE_PRIVATE)
    return prefs.getStringSet("searches", emptySet())
        ?.toList()
        ?.reversed()
        ?.take(5)
        ?: listOf()}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            var feed by remember { mutableStateOf(false) }
            var query by remember { mutableStateOf("") }
            var sources by remember { mutableStateOf(listOf(1, 1, 1, 1)) }

            val context = LocalContext.current
            var recentSearches by remember { mutableStateOf(getRecentSearches(context)) }


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
                    // Pass back parameters if FeedScreen needs them
                    FeedScreen(
                        onBackClick = { feed = false },
                        query = query,
                        sources = sources
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StreamlinedappTheme {
        Greeting("Android")
    }
}