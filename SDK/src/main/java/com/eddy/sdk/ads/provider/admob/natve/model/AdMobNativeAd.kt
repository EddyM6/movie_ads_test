package com.eddy.sdk.ads.provider.admob.natve.model

import com.google.android.gms.ads.nativead.NativeAd

/**
 * AdMob-specific implementation of NativeAd.
 */
internal class AdMobNativeAd(private val googleNativeAd: NativeAd) : com.eddy.sdk.ads.provider.native.model.NativeAd() {

    override fun getHeadline(): String? = googleNativeAd.headline

    override fun getBody(): String? = googleNativeAd.body

    override fun getCallToAction(): String? = googleNativeAd.callToAction

    override fun getAdvertiser(): String? = googleNativeAd.advertiser

    override fun getPrice(): String? = googleNativeAd.price

    override fun getStarRating(): Double? = googleNativeAd.starRating

    override fun getStore(): String? = googleNativeAd.store

    override fun getAdObject(): Any = googleNativeAd

    override fun recordImpression() {
        // Google's SDK handles this automatically
    }
}
