package com.eddy.sdk.ads.config

/**
 * Sealed class representing different ad provider types.
 */
sealed class AdProviderType {
    /**
     * Google AdMob provider type.
     */
    object AdMob : AdProviderType()
}
