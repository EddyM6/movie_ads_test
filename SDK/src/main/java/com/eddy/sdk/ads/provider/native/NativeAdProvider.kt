package com.eddy.sdk.ads.provider.native

import com.eddy.sdk.ads.provider.native.model.NativeAd
import com.eddy.sdk.ads.provider.AdProvider

interface NativeAdProvider : AdProvider {
    /**
     * Loads a native ad with the given ad unit ID.
     * @return A Result containing the native ad if successful, or an exception if failed.
     */
    suspend fun loadNativeAd(adUnitId: String): Result<NativeAd>
    
    /**
     * Checks if a native ad is currently loaded for the given ad unit ID.
     */
    fun isNativeAdLoaded(adUnitId: String): Boolean
    
    /**
     * Preloads a native ad for later use.
     */
    suspend fun preloadNativeAd(adUnitId: String)
}
