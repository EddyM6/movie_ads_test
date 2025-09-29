package com.eddy.jedymovieapptest.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eddy.sdk.ads.AdsSdk

@Composable
fun NativeAdCard(
    modifier: Modifier = Modifier
) {
    AdsSdk.getNativeAdComposable().invoke(
        modifier
            .fillMaxWidth()
    )
}
