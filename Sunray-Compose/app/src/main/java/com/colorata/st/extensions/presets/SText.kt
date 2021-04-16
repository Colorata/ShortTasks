package com.colorata.st.extensions.presets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.buttonColor
import com.colorata.st.ui.theme.productFont


@Composable
fun SText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    fontSize: TextUnit = SDimens.cardTitle,
    fontWeight: FontWeight = FontWeight.Medium,
    textColor: Color = buttonColor(LocalContext.current)
){
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = textColor,
            fontFamily = productFont
        ),
        textAlign = textAlign
    )
}


