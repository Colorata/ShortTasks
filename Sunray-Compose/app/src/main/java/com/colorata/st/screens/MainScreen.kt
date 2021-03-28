package com.colorata.st.screens

import android.content.Intent
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
import androidx.compose.ui.unit.sp
import com.colorata.st.CurrentScreen
import com.colorata.st.R
import com.colorata.st.SecondaryActivity
import com.colorata.st.ui.theme.*

@ExperimentalAnimationApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backColor(LocalContext.current))
    ) {

        val title = listOf(
            "Main", "Welcome", "Bubble Manager", "Power Assistant"
        )

        val subTitle = listOf(
            "Main", "Welcome to Colorata family!", "You will learn how to use Bubble Manager", "You will learn how to use Power Assistant"
        )

        val icon = listOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_announcement_24,
            R.drawable.ic_outline_bubble_chart_24,
            R.drawable.ic_outline_power_settings_new_24
        )
        LazyColumn(
            modifier = Modifier.fillMaxHeight()) {
            items(items = title, itemContent = { item ->
                when (item) {
                    "Main" -> {
                        Title(title = "Main", subTitle = "Related Posts")
                    }
                    else -> {
                        RelatedCard(
                            title = item,
                            subTitle = subTitle[title.indexOf(item)],
                            icon = icon[title.indexOf(item)]
                        ){
                            when(item){
                                "Welcome" -> WelcomeContent()
                                "Bubble Manager" -> BubbleMainScreenContent()
                                "Power Assistant" -> PowerMainScreenContent()
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
fun BubbleMainScreenContent(){
    Row(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier, text = "Show") {
            Log.d("Clicked", "Show")
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun PowerMainScreenContent(){
    Row(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier, text = "Show") {
            Log.d("Clicked", "Show")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun WelcomeContent(){
    val context = LocalContext.current
    Column(modifier = Modifier.padding(30.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            SText(
                text = "I prefer to use...",
                modifier = Modifier.padding(bottom = 10.dp),
                fontSize = 25.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SButton(modifier = Modifier.padding(end = 10.dp), text = "Bubble") {
                Log.d("Clicked", "Bubble")
                val intent = Intent(context, SecondaryActivity::class.java)
                intent.putExtra("Screen", CurrentScreen.BUBBLE)
                context.startActivity(intent)
            }

            SButton(modifier = Modifier, text = "Power") {
                Log.d("Clicked", "Power")
                val intent = Intent(context, SecondaryActivity::class.java)
                intent.putExtra("Screen", CurrentScreen.POWER)
                context.startActivity(intent)
            }
        }
    }
}

//Main Screen with Sticky Header
/*
@ExperimentalFoundationApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainScreen() {

    val shared = LocalContext.current.getSharedPreferences("Shared", Context.MODE_PRIVATE)

    val mainPadding = (shared.getInt("bottomSize", 30)*2).dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backColor(LocalContext.current)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val list = listOf(
            "A", "B", "Power Assistant", "D"
        ) + (0..10).map { it.toString() }
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            val grouped = list.groupBy { it[0] }
            grouped.forEach { (initial, groupedItem) ->
                stickyHeader {
                    Box(modifier = Modifier.background(color = backColor(LocalContext.current)).fillMaxWidth()) {
                        SText(text = initial.toString(), modifier = Modifier.padding(start = 30.dp))
                    }
                }
                items(items = groupedItem, itemContent = { item ->
                    when (item) {
                        "A" -> {
                            SText(
                                text = "Main", modifier = Modifier
                                    .padding(
                                        top = mainPadding,
                                        start = 20.dp,
                                        end = 30.dp
                                    )
                                    .fillMaxWidth(),
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Start
                            )

                            SText(
                                text = "Related Posts", modifier = Modifier
                                    .padding(
                                        top = 30.dp,
                                        start = 30.dp,
                                        end = 30.dp,
                                        bottom = 10.dp
                                    )
                                    .fillMaxWidth(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Light,
                                textAlign = TextAlign.Start
                            )
                        }
                        else -> {
                            RelatedCard(
                                title = item,
                                subTitle = item,
                                icon = Icons.Outlined.Search
                            )
                        }
                    }
                })
            }
        }
    }
}*/
