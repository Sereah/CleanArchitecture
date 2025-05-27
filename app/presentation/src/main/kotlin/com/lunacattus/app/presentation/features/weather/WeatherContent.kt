package com.lunacattus.app.presentation.features.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.lunacattus.app.presentation.common.theme.AppTheme

@Composable
fun WeatherContent(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "hello, weather",
            fontSize = 24.sp,
            color = AppTheme.colors.mainText
        )
    }
}

@Composable
@Preview
fun WeatherContentPreview() {
    AppTheme {
        WeatherContent()
    }
}