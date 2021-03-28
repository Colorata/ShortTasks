package com.colorata.st.ui.theme

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults.outlinedButtonColors
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colorata.st.R
import java.util.*


private val productFont = FontFamily(
    Font(R.font.font)
)

@Composable
fun SText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    fontSize: TextUnit = 30.sp,
    fontWeight: FontWeight = FontWeight.Medium
){
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = buttonColor(LocalContext.current),
            fontFamily = productFont
        ),
        textAlign = textAlign
    )
}

@ExperimentalAnimationApi
@Composable
fun RelatedCard(title: String, subTitle: String, icon: Int, hidden: @Composable () -> Unit){

    var show by remember {
        mutableStateOf(false)
    }
    Card(shape = RoundedCornerShape(30.dp),
        border = BorderStroke(width = 2.dp, color = buttonColor(LocalContext.current)),
        backgroundColor = backColor(LocalContext.current),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.clickable { show = !show }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.background),
                        contentDescription = "",
                        modifier = Modifier
                            .size(200.dp),
                        colorFilter = ColorFilter.tint(color = buttonColor(LocalContext.current))
                    )

                    Image(
                        painter = painterResource(icon),
                        contentDescription = "",
                        modifier = Modifier
                            .size(50.dp),
                        colorFilter = ColorFilter.tint(color = buttonColor(LocalContext.current))
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {

                    SText(
                        text = title,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    SText(
                        text = subTitle,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(bottom = 20.dp, end = 20.dp, start = 20.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            }
            AnimatedVisibility(
                visible = show
            ) {
                hidden()
            }
        }
    }
}

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

@Composable
fun SButton(text: String = "", modifier: Modifier, onClick: () -> Unit){
    OutlinedButton(
        onClick = onClick,
        shape = CircleShape,
        border = BorderStroke(
            width = 1.dp,
            color = BorderColor
        ),
        modifier = modifier,
        colors = outlinedButtonColors(backgroundColor = Color.Transparent)
    ) {
        SText(
            text = text.toUpperCase(Locale.ROOT),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(name = "SButton", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun TButtonDefault(){
    SButton(text = "OutLined Button", modifier = Modifier) {

    }
}

@Preview(showBackground = true, name = "Title")
@Composable
fun TitleDefault(){
    Title(title = "Title", subTitle = "SubTitle")
}

@ExperimentalAnimationApi
@Preview(name = "Related Card")
@Composable
private fun RelatedCardPreview(){
    RelatedCard(
        title = "Card Title",
        subTitle = "Card SubTitle",
        icon = R.drawable.ic_outline_wb_sunny_24
    ) {
        SText(text = "Top", modifier = Modifier.padding(30.dp))
    }
}