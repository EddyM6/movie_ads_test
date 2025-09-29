package com.eddy.sdk.ads

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.eddy.sdk.ads.config.AdConfig
import com.eddy.sdk.ads.provider.factory.InterstitialAdProviderFactory
import com.eddy.sdk.ads.provider.factory.NativeAdProviderFactory
import com.eddy.sdk.ads.provider.interstitial.InterstitialAdManager
import com.eddy.sdk.ads.provider.interstitial.strategy.DefaultInterstitialShowStrategy
import com.eddy.sdk.ads.provider.native.NativeAdProvider
import com.eddy.sdk.ads.provider.admob.natve.ui.NativeAdComposable

/**
 * Main entry point for the Ads SDK.
 * This singleton manages all ad-related functionality.
 */
object AdsSdk {
    private var isInitialized = false
    private var _adConfig: AdConfig? = null
    private var _interstitialAdManager: InterstitialAdManager? = null
    val interstitialAdManager: InterstitialAdManager
        get() {
            val interstitialAdManager = _interstitialAdManager ?: run {
                throw IllegalStateException("InterstitialAdManager is not initialized")
            }
            return interstitialAdManager
        }

    private var _nativeAdProvider: NativeAdProvider? = null

    val nativeAdProvider: NativeAdProvider
        get() {
            val nativeAdProvider = _nativeAdProvider ?: run {
                throw IllegalStateException("NativeAdProvider is not initialized")
            }
            return nativeAdProvider
        }


    fun initialize(application: Application, config: AdConfig) {
        if (isInitialized) return

        _adConfig = config
        val interstitialProvider = InterstitialAdProviderFactory(context = application).provide(config)
        _interstitialAdManager = InterstitialAdManager(
            interstitialAdProvider = interstitialProvider,
            adConfig = config,
            interstitialShowStrategy = DefaultInterstitialShowStrategy.getDefaultStrategy(application)
        )

        val nativeProvider = NativeAdProviderFactory(context = application).provide(config)
        _nativeAdProvider = nativeProvider

        isInitialized = true
    }
    
    /**
     * Returns a composable function that displays a native ad.
     */
    fun getNativeAdComposable(): @Composable (Modifier) -> Unit {
        if (!isInitialized || _adConfig == null) {
            throw IllegalStateException("AdsSdk must be initialized before updating config")
        }

        return { modifier ->
            NativeAdComposable(
                nativeAdProvider = nativeAdProvider,
                adUnitId = _adConfig!!.nativeAdUnitId,
                modifier = modifier
            )
        }
    }
}
