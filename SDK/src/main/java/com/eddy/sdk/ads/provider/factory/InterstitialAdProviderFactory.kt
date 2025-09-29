package com.eddy.sdk.ads.provider.factory

import android.content.Context
import com.eddy.sdk.ads.config.AdConfig
import com.eddy.sdk.ads.config.AdProviderType
import com.eddy.sdk.ads.provider.admob.interstitial.AdMobInterstitialAdProvider
import com.eddy.sdk.ads.provider.interstitial.InterstitialAdProvider

internal class InterstitialAdProviderFactory(private val context: Context) {
    fun provide(config: AdConfig): InterstitialAdProvider {
        when (config.providerType) {
            is AdProviderType.AdMob -> {
                return AdMobInterstitialAdProvider(context, config)
            }
        }
    }
}
