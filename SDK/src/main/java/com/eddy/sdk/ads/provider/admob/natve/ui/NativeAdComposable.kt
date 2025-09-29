package com.eddy.sdk.ads.provider.admob.natve.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.eddy.sdk.ads.provider.native.model.NativeAd
import com.eddy.sdk.ads.provider.native.NativeAdProvider
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.delay

/**
 * A Composable function that displays a native ad.
 */
@Composable
fun NativeAdComposable(
    nativeAdProvider: NativeAdProvider,
    adUnitId: String,
    modifier: Modifier = Modifier
) {
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    // Initialize the ad provider if needed
    LaunchedEffect(Unit) {
        if (!nativeAdProvider.isInitialized) {
            throw IllegalStateException("NativeAdProvider must be initialized before loading ad")
        }
    }
    
    // Load the ad
    LaunchedEffect(adUnitId) {
        if (!isLoading && nativeAd == null) {
            isLoading = true
            error = null
            
            try {
                val result = nativeAdProvider.loadNativeAd(adUnitId)
                result.onSuccess {
                    nativeAd = it
                }.onFailure {
                    error = it.message
                    // Retry after a delay
                    delay(5000)
                }
            } catch (e: Exception) {
                error = e.message
                // Retry after a delay
                delay(5000)
            } finally {
                isLoading = false
            }
        }
    }
    
    // Display the ad or a placeholder
    Box(modifier = modifier.fillMaxWidth()) {
        when {
            nativeAd != null -> NativeAdContent(nativeAd!!)
            isLoading -> AdLoadingPlaceholder()
            error != null -> AdErrorPlaceholder(error!!) {
                error = null
                isLoading = true
            }
            else -> AdLoadingPlaceholder()
        }
    }
}

@Composable
private fun NativeAdContent(nativeAd: NativeAd) {
    val adObject = nativeAd.getAdObject() as? com.google.android.gms.ads.nativead.NativeAd ?: return
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Box(
                modifier = Modifier
                    .background(Color.Yellow.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Ad",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = nativeAd.getHeadline() ?: "",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Body
            nativeAd.getBody()?.let { body ->
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                nativeAd.getAdvertiser()?.let { advertiser ->
                    Text(
                        text = advertiser,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                nativeAd.getCallToAction()?.let { cta ->
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(text = cta)
                    }
                }
            }
            
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.dp),
                factory = { context ->
                    NativeAdView(context).apply {
                        headlineView = null
                        callToActionView = null
                        bodyView = null
                        advertiserView = null
                        
                        setNativeAd(adObject)
                    }
                }
            )
        }
    }
    
    LaunchedEffect(nativeAd) {
        nativeAd.recordImpression()
    }
}

@Composable
private fun AdLoadingPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Loading Ad...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun AdErrorPlaceholder(error: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Failed to load ad: ${error.takeIf { it.isNotBlank() } ?: "Unknown error"}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}
