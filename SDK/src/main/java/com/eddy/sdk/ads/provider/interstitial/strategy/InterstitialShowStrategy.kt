package com.eddy.sdk.ads.provider.interstitial.strategy

interface InterstitialShowStrategy {
    suspend fun shouldShow(): Boolean
    suspend fun reset()
}