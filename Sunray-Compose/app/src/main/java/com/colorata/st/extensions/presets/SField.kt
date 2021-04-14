package com.colorata.st.extensions.presets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backColor
import com.colorata.st.ui.theme.buttonColor

@Composable
fun SField(label: String, modifier: Modifier = Modifier, onValueChange: (TextFieldValue) -> Unit) {

    var textValue by remember { mutableStateOf(TextFieldValue())}

    Card(shape = CircleShape,
        border = BorderStroke(width = SDimens.borderWidth, color = buttonColor(LocalContext.current)),
        backgroundColor = backColor(LocalContext.current),
        modifier = modifier
    ) {
        TextField(
            value = textValue,
            onValueChange = { textFieldValue ->
                textValue = textFieldValue
                onValueChange(textFieldValue)
            },
            singleLine = true,
            label = {
                SText(text = label, fontSize = SDimens.buttonText)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = backColor(LocalContext.current),
                unfocusedBorderColor = backColor(LocalContext.current),
                backgroundColor = backColor(LocalContext.current),
                cursorColor = buttonColor(LocalContext.current),
                textColor = buttonColor(LocalContext.current),
            ),
            modifier = Modifier.padding(horizontal = SDimens.smallPadding)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SFieldDefault() {
    SField(
        label = Strings.city,
        modifier = Modifier.padding(SDimens.largePadding)
    ) {
    }
}
