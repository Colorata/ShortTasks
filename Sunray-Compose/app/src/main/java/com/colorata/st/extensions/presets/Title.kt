package com.colorata.st.extensions.presets

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Title(title: String, subTitle: String){
    val shared = LocalContext.current.getSharedPreferences("Shared", Context.MODE_PRIVATE)

    val mainPadding = (shared.getInt("bottomSize", 30)*2).dp
    Column {
        SText(
            text = title, modifier = Modifier
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
            text = subTitle, modifier = Modifier
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
}



@Preview(showBackground = true, name = "Title")
@Composable
fun TitleDefault(){
    Title(title = "Title", subTitle = "SubTitle")
}
