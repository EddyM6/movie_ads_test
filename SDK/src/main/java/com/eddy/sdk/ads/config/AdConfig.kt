package com.eddy.sdk.ads.config

import android.content.Context

/**
 * Configuration for the Ads SDK.
 */
class AdConfig private constructor(builder: Builder) {
    val providerType: AdProviderType = builder.providerType

    val adMobAppId: String = builder.adMobAppId

    val nativeAdUnitId: String = builder.nativeAdUnitId

    val interstitialAdUnitId: String = builder.interstitialAdUnitId

    val isTestMode: Boolean = builder.isTestMode

    class Builder(private val context: Context) {
        internal var providerType: AdProviderType = AdProviderType.AdMob
        internal var adMobAppId: String = "ca-app-pub-3940256099942544~3347511713" // Default test app ID
        internal var nativeAdUnitId: String = "ca-app-pub-3940256099942544/2247696110" // Default test native ad unit ID
        internal var interstitialAdUnitId: String = "ca-app-pub-3940256099942544/1033173712" // Default test interstitial ad unit ID
        internal var isTestMode: Boolean = true

        fun setProviderType(providerType: AdProviderType) = apply { this.providerType = providerType }

        fun setAdMobAppId(adMobAppId: String) = apply { this.adMobAppId = adMobAppId }

        fun setNativeAdUnitId(nativeAdUnitId: String) = apply { this.nativeAdUnitId = nativeAdUnitId }

        fun setInterstitialAdUnitId(interstitialAdUnitId: String) = apply { this.interstitialAdUnitId = interstitialAdUnitId }

        fun setTestMode(isTestMode: Boolean) = apply { this.isTestMode = isTestMode }

        fun build(): AdConfig = AdConfig(this)
    }
    companion object {
        fun builder(context: Context): Builder = Builder(context)
    }
}
