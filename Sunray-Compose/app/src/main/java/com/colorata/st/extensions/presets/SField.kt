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
import com.colorata.st.ui.theme.*

@Composable
fun SField(label: String, modifier: Modifier = Modifier, onValueChange: (TextFieldValue) -> Unit) {

    var textValue by remember { mutableStateOf(TextFieldValue())}

    Card(shape = CircleShape,
        border = BorderStroke(width = SDimens.borderWidth, color = foregroundColor()),
        backgroundColor = backgroundColor(),
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
                focusedBorderColor = backgroundColor(),
                unfocusedBorderColor = backgroundColor(),
                backgroundColor = backgroundColor(),
                cursorColor = foregroundColor(),
                textColor = foregroundColor(),
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
