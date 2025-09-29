package com.eddy.sdk.ads.provider.native.model

/**
 * Abstract representation of a native ad.
 */
abstract class NativeAd {
    abstract fun getHeadline(): String?

    abstract fun getBody(): String?

    abstract fun getCallToAction(): String?

    abstract fun getAdvertiser(): String?

    abstract fun getPrice(): String?

    abstract fun getStarRating(): Double?

    abstract fun getStore(): String?

    abstract fun getAdObject(): Any

    abstract fun recordImpression()
}
