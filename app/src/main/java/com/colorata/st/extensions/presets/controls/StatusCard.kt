package com.colorata.st.extensions.presets.controls

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colorata.st.R
import com.colorata.st.extensions.*
import com.colorata.st.extensions.presets.SText
import com.colorata.st.extensions.weather.WeatherResponse
import com.colorata.st.extensions.weather.WeatherService
import com.colorata.st.ui.theme.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt


@Composable
fun StatusCard(modifier: Modifier = Modifier) {
    var currentRealTemp by remember { mutableStateOf(0) }
    var currentFeelsTemp by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var isLaunched by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var currentIcon: ImageBitmap? by remember { mutableStateOf(null) }
    LaunchedEffect(key1 = true) {
        isLaunched = true
        newThread {
            city = SuperStore(context).catchString(Strings.city, "Moscow")

            //Building service to get info
            val retrofit = Retrofit.Builder()
                .baseUrl(Strings.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(WeatherService::class.java)
            val call = service.getCurrentWeatherData(city, "metric", Strings.appId)

            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {

                    if (response.code() == 200) {
                        val weatherResponse = response.body()!!
                        currentRealTemp = weatherResponse.main?.temp?.roundToInt()!!
                        currentFeelsTemp = "Feels like " + weatherResponse.main?.feels?.roundToInt()
                            .toString() + " ${Strings.degreeIcon}"
                        newThread {
                            currentIcon = getCurrentWeatherImage(weatherResponse.weather[0].icon)
                        }
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    println("Fail")
                }
            })
        }
    }
    val displayWidthPixels by remember { mutableStateOf(context.resources.displayMetrics.widthPixels) }
    Box(modifier = modifier
        .fillMaxWidth()
        .height(pxToDp(displayWidthPixels).dp * 0.8f)) {
        val transition = rememberInfiniteTransition()
        val circleAngle by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(animation = tween(60000, easing = LinearEasing))
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(SDimens.largePadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pill_you),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .size(150.dp),
                colorFilter = ColorFilter.tint(Color(SystemColor.BLACK.primaryHex.toIntColor()))
            )
            Box(contentAlignment = Alignment.Center, modifier = Modifier.matchParentSize()) {
                if (currentIcon != null) {
                    Image(
                        bitmap = currentIcon!!,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(start = 30.dp, bottom = 30.dp)
                            .size(96.dp)
                            .align(Alignment.BottomStart)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_outline_cloud_24),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(bottom = 30.dp, start = 30.dp)
                            .size(96.dp)
                            .align(Alignment.BottomStart)
                    )
                }
                val animatedTemp by animateIntAsState(
                    targetValue = if (!isLaunched) 0 else currentRealTemp,
                    animationSpec = tween(1000, 100)
                )
                SText(
                    text = "$animatedTemp ${Strings.rawDegreeIcon}",
                    fontSize = 40.sp,
                    modifier = Modifier
                        .padding(top = 30.dp, end = 30.dp)
                        .align(Alignment.TopEnd),
                    textColor = Color(SystemColor.BLACK.secondaryHex.toIntColor())
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(SDimens.largePadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.circle_you),
                contentDescription = "",
                modifier = Modifier
                    .rotate(circleAngle)
                    .size(170.dp),
                colorFilter = ColorFilter.tint(Color(SystemColor.BLACK.primaryHex.toIntColor()))
            )
            val animatedHour by animateFloatAsState(
                targetValue = if (!isLaunched) -180f
                else getTime().first.toFloat() * 30 - 180,
                animationSpec = tween(2000, 300)
            )
            val animatedMinute by animateFloatAsState(
                targetValue = if (!isLaunched) 0f
                else getTime().second.toFloat() * 6 - 180,
                animationSpec = tween(2000, 300)
            )
            Canvas(modifier = Modifier.size(120.dp)) {
                drawIntoCanvas {
                    val path = Path().apply {
                        addArc(0f, 0f, 120.dp.toPx(), 120.dp.toPx(), 180f, 180f)
                    }
                    it.nativeCanvas.drawTextOnPath(
                        getDate(),
                        path,
                        0f,
                        0f,
                        Paint().apply {
                            textSize = 16.sp.toPx()
                            textAlign = Paint.Align.CENTER
                            typeface = context.resources.getFont(R.font.font)
                        }
                    )
                }
                rotate(
                    animatedMinute, pivot = Offset(
                        size.width / 2 - 0.dp.toPx(),
                        size.height / 2 - 0.dp.toPx()
                    )
                ) {
                    drawRoundRect(
                        Color(context.foregroundInt()),
                        topLeft = Offset(
                            size.width / 2 - 5.dp.toPx(),
                            size.height / 2 - 5.dp.toPx()
                        ),
                        size = Size(10.dp.toPx(), size.height / 2.5f),
                        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                    )
                }

                rotate(
                    animatedHour, pivot = Offset(
                        size.width / 2 - 0.dp.toPx(),
                        size.height / 2 - 0.dp.toPx()
                    )
                ) {
                    drawRoundRect(
                        Color(context.trueBackgroundInt()),
                        topLeft = Offset(
                            size.width / 2 - 5.dp.toPx(),
                            size.height / 2 - 5.dp.toPx()
                        ),
                        size = Size(10.dp.toPx(), size.height / 3),
                        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                    )
                }
                /*drawArc(
                    Color(SystemColor.BLACK.secondaryHex.toIntColor()),
                    -90f,
                    if (getTime().first.toFloat() > 12) (getTime().first.toFloat() - 12) * 30
                    else getTime().first.toFloat() * 30,
                    false,
                    topLeft = Offset(0f, 0f),
                    size = Size(120.dp.toPx(), 120.dp.toPx()),
                    style = Stroke(15.dp.toPx(), cap = StrokeCap.Round)
                )
                val minutesSize = 80.dp.toPx()
                drawArc(
                    Color(SystemColor.BLACK.secondaryHex.toIntColor()),
                    -90f,
                    getTime().second.toFloat() * 5,
                    false,
                    topLeft = Offset(
                        (size.width - minutesSize) / 2,
                        (size.height - minutesSize) / 2
                    ),
                    size = Size(minutesSize, minutesSize),
                    style = Stroke(7.5.dp.toPx(), cap = StrokeCap.Round)
                )*/
            }
        }
    }
    /*Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .padding(SDimens.smallPadding)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = context.getCurrentBatteryIcon()),
                contentDescription = "",
                modifier = Modifier.padding(end = SDimens.smallPadding, top = SDimens.smallPadding)
            )
            Column {
                SText(text = "${context.getBatteryPercentage()}%", fontSize = SDimens.statusCardText)
                SText(
                    text = context.getNextBatteryFormat(),
                    fontSize = SDimens.buttonText,
                    textColor = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(end = SDimens.smallPadding)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(end = SDimens.normalPadding)
                .fillMaxWidth()
        ) {
            if (currentIcon != null) {
                Image(
                    bitmap = currentIcon!!,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(SDimens.smallPadding)
                        .size(96.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_outline_cloud_24),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(SDimens.smallPadding)
                        .size(96.dp)
                )
            }
            Column {
                SText(text = "$city, $currentRealTemp", fontSize = SDimens.statusCardText)
                SText(
                    text = currentFeelsTemp,
                    fontSize = SDimens.buttonText,
                    textColor = Color.White.copy(alpha = 0.5f)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(SDimens.normalPadding)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_outline_access_time_24),
                contentDescription = "",
                modifier = Modifier.padding(SDimens.smallPadding)
            )
            Column {
                SText(text = context.getTimeFormat(), fontSize = SDimens.statusCardText)
                SText(
                    text = getDate(),
                    fontSize = SDimens.buttonText,
                    textColor = Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }*/
}