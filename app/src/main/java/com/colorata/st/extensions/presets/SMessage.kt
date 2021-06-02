package com.colorata.st.extensions.presets

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.Strings

@ExperimentalAnimationApi
@Composable
fun SMessage(
    modifier: Modifier = Modifier,
    visible: Boolean,
    text: String,
    buttonText: String = Strings.ok,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SText(
                text = text,
                fontSize = SDimens.subTitle,
                modifier = Modifier
                    .padding(end = SDimens.smallPadding, top = SDimens.smallPadding)
                    .weight(2f)

            )
            SButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = SDimens.smallPadding),
                text = buttonText
            ) {
                onClick()
            }
        }
    }
}