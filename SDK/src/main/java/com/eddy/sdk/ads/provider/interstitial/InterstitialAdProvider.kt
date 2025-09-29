package com.eddy.sdk.ads.provider.interstitial

import android.app.Activity
import com.eddy.sdk.ads.provider.AdProvider

interface InterstitialAdProvider : AdProvider {
    /**
     * Loads an interstitial ad with the given ad unit ID.
     * @return A Result indicating whether the ad was successfully loaded.
     */
    suspend fun loadInterstitialAd(adUnitId: String): Result<Boolean>
    
    /**
     * Shows an interstitial ad for the given activity.
     * @return A Result indicating whether the ad was successfully shown.
     */
    suspend fun showInterstitialAd(activity: Activity, adUnitId: String): Result<Boolean>
    
    /**
     * Checks if an interstitial ad is currently loaded for the given ad unit ID.
     */
    fun isInterstitialAdLoaded(adUnitId: String): Boolean
}
