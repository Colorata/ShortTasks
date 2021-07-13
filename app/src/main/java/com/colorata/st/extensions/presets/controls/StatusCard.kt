package com.colorata.st.extensions.presets.controls

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
    var currentRealTemp by remember { mutableStateOf("") }
    var currentFeelsTemp by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    val context = LocalContext.current
    var currentIcon: ImageBitmap? by remember { mutableStateOf(null) }
    LaunchedEffect(key1 = true) {
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
                        currentRealTemp = weatherResponse.main?.temp?.roundToInt()
                            .toString() + " ${Strings.degreeIcon}"
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
    Card(
        shape = RoundedCornerShape(SDimens.roundedCorner),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f),
        backgroundColor = Color(SystemColor.BLACK.secondaryHex.toIntColor())
    ) {
        Column(
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
        }
    }
}