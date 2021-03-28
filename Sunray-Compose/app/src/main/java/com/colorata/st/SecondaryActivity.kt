package com.colorata.st

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.screens.BubbleMainScreenContent
import com.colorata.st.screens.PowerMainScreenContent
import com.colorata.st.screens.WelcomeContent
import com.colorata.st.ui.theme.RelatedCard
import com.colorata.st.ui.theme.Title
import com.colorata.st.ui.theme.backColor
import com.colorata.st.ui.theme.backInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SecondaryActivity : AppCompatActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SecondaryContent(intent)
            }
        }

        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        window.setDecorFitsSystemWindows(false)

    }
}

@ExperimentalAnimationApi
@Composable
fun SecondaryContent(intent: Intent) {

    var text = "Secondary"
    if (intent.getSerializableExtra("Screen") == CurrentScreen.BUBBLE){
        Log.d("Intent", "Bubble")
    } else Log.d("Intent", "Power")

    when(intent.getSerializableExtra("Screen")){
        CurrentScreen.BUBBLE -> text = "Bubble Manager"
        CurrentScreen.POWER -> text = "Power Assistant"
    }
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
                        Title(title = text, subTitle = "Related Posts")
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