package com.colorata.st.extensions.presets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.R
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.foregroundColor

@Composable
fun SControl(title: String, icon: Int){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(SDimens.smallPadding)
    ) {
        OutlinedButton(
            onClick = { /*TODO*/ },
            shape = CircleShape,
            modifier = Modifier.size(SDimens.elementSize),
            border = BorderStroke(
                width = SDimens.borderWidth,
                color = foregroundColor()
            ),
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "",
                tint = foregroundColor(),
                modifier = Modifier.size(SDimens.postImageSize)
            )
        }
        SText(text = title, fontSize = SDimens.buttonText)
    }
}

@Preview
@Composable
private fun SControlDefault(){
    MaterialTheme {
        SControl(title = Strings.flashlight, icon = R.drawable.ic_outline_flash_on_24)
    }
}