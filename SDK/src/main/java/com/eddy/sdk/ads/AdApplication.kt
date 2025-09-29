package com.eddy.sdk.ads

import android.app.Activity
import android.app.Application
import androidx.annotation.CallSuper
import com.eddy.sdk.R
import com.eddy.sdk.ads.config.AdConfig
import com.eddy.sdk.ads.config.AdProviderType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Base Application class that handles ad-related functionality.
 * App module's Application class should extend this class to enable ad functionality.
 */
abstract class AdApplication(private val isDebugMode: Boolean = true) : Application() {
    private val configBuilder: AdConfig.Builder by lazy { getDefaultConfigBuilder(isDebugMode) }

    private val adScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    @CallSuper
    override fun onCreate() {
        super.onCreate()

        setupAdsConfig(configBuilder)
        
        AdsSdk.initialize(
            this,
            configBuilder.build()
        )

        tryToOpenInterstitialOnAppStart()
    }
    
    /**
     * Sets up the app open counter to track when to show interstitial ads
     */
    private fun tryToOpenInterstitialOnAppStart() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: android.os.Bundle?) {}
            
            override fun onActivityStarted(activity: Activity) {
                adScope.launch {
                    AdsSdk.interstitialAdManager.showInterstitialAdIfAvailable(activity)
                }
            }
            
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: android.os.Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun getDefaultConfigBuilder(isDebugMode: Boolean): AdConfig.Builder {
        return AdConfig.builder(this)
            .setProviderType(AdProviderType.AdMob)
            .setTestMode(isDebugMode)
            .setAdMobAppId(this.getString(R.string.default_admob_app_id))
            .setNativeAdUnitId(this.getString(R.string.default_admob_native_ad_unit_id))
            .setInterstitialAdUnitId(this.getString(R.string.default_admob_interstitial_ad_unit_id))
    }

    /**
     * Sets up the ads configuration, can be overridden by the app module's Application class
     */
    open fun setupAdsConfig(builder: AdConfig.Builder) {

    }
}
