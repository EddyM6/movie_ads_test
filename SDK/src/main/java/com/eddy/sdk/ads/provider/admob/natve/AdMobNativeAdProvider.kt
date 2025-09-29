package com.eddy.sdk.ads.provider.admob.natve

import android.content.Context
import com.eddy.sdk.ads.config.AdConfig
import com.eddy.sdk.ads.provider.admob.natve.model.AdMobNativeAd
import com.eddy.sdk.ads.provider.native.model.NativeAd
import com.eddy.sdk.ads.provider.native.NativeAdProvider
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.nativead.NativeAdOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Collections
import kotlin.coroutines.resume

/**
 * Implementation of NativeAdProvider for Google AdMob.
 */
internal class AdMobNativeAdProvider(
    private val context: Context,
    private val config: AdConfig,
) : NativeAdProvider {
    override var isInitialized = false
        private set
    
    private val loadedAds = mutableMapOf<String, AdMobNativeAd>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        scope.launch {
            initialize(context, config)
        }
    }
    
    override suspend fun initialize(context: Context, config: AdConfig) {
        if (isInitialized) return

        withContext(Dispatchers.Main) {
            if (config.isTestMode) {
                val testDeviceIds = Collections.singletonList(
                    AdRequest.DEVICE_ID_EMULATOR
                )
                val configuration = RequestConfiguration.Builder()
                    .setTestDeviceIds(testDeviceIds)
                    .build()
                MobileAds.setRequestConfiguration(configuration)
            }
            
            MobileAds.initialize(context) {
                isInitialized = true
            }
        }
    }
    
    override suspend fun loadNativeAd(adUnitId: String): Result<NativeAd> = 
        withContext(Dispatchers.Main) {
            try {
                if (!isInitialized) {
                    return@withContext Result.failure(IllegalStateException("AdMobNativeAdProvider not initialized"))
                }
                
                // Check if we already have a cached ad
                loadedAds[adUnitId]?.let {
                    return@withContext Result.success(it)
                }
                
                // Load a new ad
                val adMobNativeAd = suspendCancellableCoroutine { continuation ->
                    val adLoader = AdLoader.Builder(this@AdMobNativeAdProvider.context, adUnitId)
                        .forNativeAd { nativeAd ->
                            val ad = AdMobNativeAd(nativeAd)
                            loadedAds[adUnitId] = ad
                            if (continuation.isActive) {
                                continuation.resume(ad)
                            } else {
                                nativeAd.destroy()
                            }
                        }
                        .withAdListener(createAdListener(adUnitId) {
                            if (continuation.isActive) {
                                continuation.resume(null)
                            }
                        })
                        .withNativeAdOptions(
                            NativeAdOptions.Builder()
                                .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                                .build()
                        )
                        .build()
                    
                    adLoader.loadAd(AdRequest.Builder().build())
                }
                
                if (adMobNativeAd != null) {
                    Result.success(adMobNativeAd)
                } else {
                    Result.failure(Exception("Failed to load native ad"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    override fun isNativeAdLoaded(adUnitId: String): Boolean {
        return loadedAds.containsKey(adUnitId)
    }
    
    override suspend fun preloadNativeAd(adUnitId: String) {
        if (!isNativeAdLoaded(adUnitId)) {
            loadNativeAd(adUnitId)
        }
    }
    
    private fun createAdListener(adUnitId: String, onFailure: () -> Unit): AdListener {
        return object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                onFailure()
            }
            
            override fun onAdClosed() {
                loadedAds.remove(adUnitId)
            }
        }
    }
}
