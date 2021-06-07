package com.colorata.st.extensions.presets

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colorata.st.ui.theme.SDimens
import com.colorata.st.ui.theme.Strings
import com.colorata.st.ui.theme.SuperStore

@ExperimentalAnimationApi
@Composable
fun Title(title: String, subTitle: String) {
    val context = LocalContext.current

    var isVisible by remember { mutableStateOf(false) }

    val mainPadding = (SuperStore(context).catchInt(Strings.bottomSize, 30) * 1.5).dp
    LaunchedEffect(key1 = true) {
        isVisible = true
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(700)) + expandVertically(animationSpec = tween(700))
    ) {
        Column {
            SText(
                text = title, modifier = Modifier
                    .padding(
                        top = mainPadding,
                        start = SDimens.normalPadding,
                        end = SDimens.largePadding
                    )
                    .fillMaxWidth(),
                fontSize = SDimens.screenTitle,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Start
            )

            SText(
                text = subTitle, modifier = Modifier
                    .padding(
                        top = SDimens.largePadding,
                        start = SDimens.largePadding,
                        end = SDimens.largePadding,
                        bottom = SDimens.smallPadding
                    )
                    .fillMaxWidth(),
                fontSize = SDimens.subTitle,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Start
            )
        }
    }
}


@ExperimentalAnimationApi
@Preview(showBackground = true, name = "Title")
@Composable
fun TitleDefault() {
    Title(title = "Title", subTitle = "SubTitle")
}
