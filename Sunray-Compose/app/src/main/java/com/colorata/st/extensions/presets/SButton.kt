package com.colorata.st.extensions.presets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colorata.st.ui.theme.BorderColor
import java.util.*

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
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
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

