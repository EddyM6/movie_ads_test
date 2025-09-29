package com.eddy.sdk.ads.provider.factory

import android.content.Context
import com.eddy.sdk.ads.config.AdConfig
import com.eddy.sdk.ads.config.AdProviderType
import com.eddy.sdk.ads.provider.admob.natve.AdMobNativeAdProvider
import com.eddy.sdk.ads.provider.native.NativeAdProvider

internal class NativeAdProviderFactory(private val context: Context) {
    fun provide(config: AdConfig): NativeAdProvider {
        when (config.providerType) {
            is AdProviderType.AdMob -> {
                return AdMobNativeAdProvider(context, config)
            }
        }
    }
}

