package com.colorata.st.extensions.presets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.backgroundColor
import com.colorata.st.ui.theme.foregroundColor

@ExperimentalComposeUiApi
@Composable
fun SField(
    label: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onValueChange: (TextFieldValue) -> Unit
) {

    var textValue by remember { mutableStateOf(TextFieldValue()) }
    val controller = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    focus.moveFocus(FocusDirection.Down)
    Card(
        shape = CircleShape,
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
            modifier = Modifier.padding(horizontal = SDimens.smallPadding),
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            keyboardActions = KeyboardActions(onDone = {
                controller?.hide()
            }, onNext = {
                focus.moveFocus(FocusDirection.Down)
            })
        )
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SFieldDefault() {
    SField(
        label = Strings.city,
        modifier = Modifier.padding(SDimens.largePadding)
    ) {
    }
}
