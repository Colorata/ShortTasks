package com.colorata.st.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.R
import com.colorata.st.extensions.ShowBubble
import com.colorata.st.ui.theme.RelatedCard
import com.colorata.st.ui.theme.SButton
import com.colorata.st.ui.theme.Title
import com.colorata.st.ui.theme.backColor

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FeatureScreen() {

    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backColor(context))
    ) {

        val title = listOf(
            "Main", "Bubble Manager", "Weather Director", "Power Assistant"
        )

        val subTitle = listOf(
            "Main", "Bubble Manager settings", "Weather Director settings", "Power Assistant settings"
        )

        val icon = listOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_bubble_chart_24,
            R.drawable.ic_outline_cloud_24,
            R.drawable.ic_outline_power_settings_new_24
        )
        LazyColumn(
            modifier = Modifier.fillMaxHeight()) {
            items(items = title, itemContent = { item ->
                when (item) {
                    "Main" -> {
                        Title(title = "Features", subTitle = "Related Posts")
                    }
                    else -> {
                        RelatedCard(
                            title = item,
                            subTitle = subTitle[title.indexOf(item)],
                            icon = icon[title.indexOf(item)]
                        ){
                            when (item) {
                                "Bubble Manager" -> BubbleManagerContent()
                                "Weather Director" -> WeatherDirectorContent()
                                "Power Assistant" -> PowerAssitantContent()
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
fun BubbleManagerContent(){

    val context = LocalContext.current
    Column(modifier = Modifier.padding(30.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = 10.dp, bottom = 10.dp), text = "Position") {
                Log.d("Clicked", "Position")
            }

            SButton(modifier = Modifier, text = "Add") {
                Log.d("Clicked", "Add")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = 10.dp), text = "Enable") {
                Log.d("Clicked", "Enable")
                ShowBubble(context)
            }

            SButton(modifier = Modifier, text = "Help") {
                Log.d("Clicked", "Help")
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun WeatherDirectorContent(){
    Row(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier.padding(end = 10.dp), text = "City") {
            Log.d("Clicked", "City")
        }

        SButton(modifier = Modifier, text = "Help") {
            Log.d("Clicked", "Help")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun PowerAssitantContent(){
    Row(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier.padding(end = 10.dp), text = "Edit") {
            Log.d("Clicked", "Edit")
        }

        SButton(modifier = Modifier, text = "Help") {
            Log.d("Clicked", "Help")
        }
    }
}

//TODO: fix bug with LazyList in LazyList crush
/*
@ExperimentalFoundationApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun BubbleContent() {
    val context = LocalContext.current

    val interactions = mutableListOf(
        "Position", "Add", "Enable", "More"
    )
    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        items(interactions){ item ->
            SButton(modifier = Modifier.padding(10.dp), text = item) {
                Toast.makeText(context, "$item clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}*/
