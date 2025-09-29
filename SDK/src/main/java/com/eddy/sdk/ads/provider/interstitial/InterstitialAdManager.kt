package com.eddy.sdk.ads.provider.interstitial

import android.app.Activity
import android.util.Log
import com.eddy.sdk.ads.config.AdConfig
import com.eddy.sdk.ads.provider.interstitial.strategy.InterstitialShowStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Manager class for interstitial ads.
 * Handles loading, caching, and displaying interstitial ads.
 */
class InterstitialAdManager(
    private val interstitialAdProvider: InterstitialAdProvider,
    private val adConfig: AdConfig,
    private val interstitialShowStrategy: InterstitialShowStrategy
) {
    private val adScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    init {
        // Preload an interstitial ad
        adScope.launch {
            preloadInterstitialAd()
        }
    }
    
    /**
     * Shows an interstitial ad if one is available, and loads the next one.
     */
    suspend fun showInterstitialAdIfAvailable(activity: Activity): Boolean {
        if (!interstitialShowStrategy.shouldShow()) {
            return false
        }
        val adUnitId = adConfig.interstitialAdUnitId
        
        if (!interstitialAdProvider.isInterstitialAdLoaded(adUnitId)) {
            // Try to load an ad
            delay(1000)
            val loadResult =interstitialAdProvider.loadInterstitialAd(adUnitId)
            if (!loadResult.isSuccess) {
                return false
            }
        }
        
        val result = interstitialAdProvider.showInterstitialAd(activity, adUnitId)
        
        adScope.launch {
            preloadInterstitialAd()
        }
        
        return result.isSuccess
    }

    private suspend fun preloadInterstitialAd() {
        val adUnitId = adConfig.interstitialAdUnitId
        
        // Don't try to load if one is already loaded
        if (!interstitialAdProvider.isInterstitialAdLoaded(adUnitId)) {
            delay(1000)
            interstitialAdProvider.loadInterstitialAd(adUnitId)
        }
    }
}
