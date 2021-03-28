package com.colorata.st.screens

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.R
import com.colorata.st.ui.theme.RelatedCard
import com.colorata.st.ui.theme.SButton
import com.colorata.st.ui.theme.Title
import com.colorata.st.ui.theme.backColor

@ExperimentalAnimationApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MoreScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backColor(LocalContext.current))
    ) {

        val title = listOf(
            "More", "Settings", "Help", "About"
        )

        val subTitle = listOf(
            "More", "ShortTasks settings", "Didn't understand?", "About ShortTasks"
        )

        val icon = listOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_settings_24,
            R.drawable.ic_outline_help_outline_24,
            R.drawable.ic_outline_info_24
        )
        LazyColumn(
            modifier = Modifier.fillMaxHeight()) {
            items(items = title, itemContent = { item ->
                when (item) {
                    "More" -> {
                        Title(title = item, subTitle = "Related Posts")
                    }
                    else -> {
                        RelatedCard(
                            title = item,
                            subTitle = subTitle[title.indexOf(item)],
                            icon = icon[title.indexOf(item)]
                        ){
                            when(item){
                                "Settings" -> SettingsContent()
                                "Help" -> HelpContent()
                                "About" -> AboutContent()
                            }
                        }
                    }
                }
            })
        }

    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun SettingsContent(){
    Row(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier, text = "Erase") {
            Log.d("Clicked", "Erase")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun HelpContent(){
    Column(modifier = Modifier.padding(30.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = 10.dp, bottom = 10.dp), text = "Bubble") {
                Log.d("Clicked", "Bubble")
            }

            SButton(modifier = Modifier, text = "Power") {
                Log.d("Clicked", "Power")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier, text = "Weather") {
                Log.d("Clicked", "Weather")
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun AboutContent(){
    Column(modifier = Modifier.padding(30.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = 10.dp, bottom = 10.dp), text = "Feedback") {
                Log.d("Clicked", "Feedback")
            }

            SButton(modifier = Modifier, text = "Donation") {
                Log.d("Clicked", "Donation")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier, text = "Version") {
                Log.d("Clicked", "Weather")
            }
        }
    }
}