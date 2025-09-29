package com.eddy.sdk.ads.provider

import android.content.Context
import com.eddy.sdk.ads.config.AdConfig

interface AdProvider {
    /**
     * Initializes the ad provider with the given context and configuration.
     */
    suspend fun initialize(context: Context, config: AdConfig)
    
    /**
     * Returns whether the ad provider has been initialized.
     */
    val isInitialized: Boolean
}
