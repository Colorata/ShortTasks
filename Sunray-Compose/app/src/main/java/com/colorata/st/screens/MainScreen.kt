package com.colorata.st.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.CurrentScreen
import com.colorata.st.activities.SecondaryActivity
import com.colorata.st.extensions.getBottomNavigationHeight
import com.colorata.st.extensions.presets.SButton
import com.colorata.st.extensions.presets.Screen
import com.colorata.st.extensions.presets.TButtonDefault
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.ScreenComponents
import com.colorata.st.ui.theme.Strings

@ExperimentalAnimationApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainScreen() {
    Screen(
        titles = ScreenComponents.MainScreen.titles,
        subTitles = ScreenComponents.MainScreen.subTitles,
        icons = ScreenComponents.MainScreen.icons,
        modifier = Modifier.padding(bottom = getBottomNavigationHeight()),
        hidden = listOf({ TButtonDefault() }, { GetStartedCardContent()}, { PowerMainScreenContent()})
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun PowerMainScreenContent(){
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(SDimens.largePadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier, text = Strings.show) {
            val intent = Intent(context, SecondaryActivity::class.java)
            intent.putExtra(Strings.screen, CurrentScreen.POWER)
            context.startActivity(intent)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, group = "Hidden Content")
@Composable
fun GetStartedCardContent(){
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(SDimens.largePadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        SButton(modifier = Modifier, text = Strings.modifySettings) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
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
