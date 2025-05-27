package com.lunacattus.app.presentation.features.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lunacattus.app.presentation.common.theme.AppTheme

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "hello, home",
            modifier = modifier.clickable {
                onClick()
            },
            color = AppTheme.colors.mainText
        )
    }
}

@Composable
@Preview
fun HomeContentPreview() {
    AppTheme {
        HomeContent {}
    }
}