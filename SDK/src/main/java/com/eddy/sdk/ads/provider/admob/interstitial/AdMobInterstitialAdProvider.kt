package com.eddy.sdk.ads.provider.admob.interstitial

import android.app.Activity
import android.content.Context
import com.eddy.sdk.ads.config.AdConfig
import com.eddy.sdk.ads.provider.interstitial.InterstitialAdProvider
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Collections
import kotlin.coroutines.resume

/**
 * Implementation of InterstitialAdProvider for Google AdMob.
 */
internal class AdMobInterstitialAdProvider(
    private val context: Context,
    private val config: AdConfig,
) : InterstitialAdProvider {
    override var isInitialized = false
        private set
    private val loadedInterstitialAds = mutableMapOf<String, InterstitialAd>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        scope.launch {
            initialize(context, config)
        }
    }

    override suspend fun initialize(context: Context, config: AdConfig) {
        if (isInitialized) return

        withContext(Dispatchers.Main) {
            // Configure AdMob with test device IDs if in test mode
            if (config.isTestMode) {
                val testDeviceIds = Collections.singletonList(
                    AdRequest.DEVICE_ID_EMULATOR
                )
                val configuration = RequestConfiguration.Builder()
                    .setTestDeviceIds(testDeviceIds)
                    .build()
                MobileAds.setRequestConfiguration(configuration)
            }

            // Initialize AdMob
            MobileAds.initialize(context) {
                isInitialized = true
            }
        }
    }

    override suspend fun loadInterstitialAd(adUnitId: String): Result<Boolean> =
        withContext(Dispatchers.Main) {
            try {
                if (!isInitialized) {
                    return@withContext Result.failure(IllegalStateException("AdMobInterstitialAdProvider not initialized"))
                }

                // Don't load if we already have one
                if (loadedInterstitialAds.containsKey(adUnitId)) {
                    return@withContext Result.success(true)
                }

                suspendCancellableCoroutine { continuation ->
                    val request = AdRequest.Builder().build()

                    InterstitialAd.load(
                        this@AdMobInterstitialAdProvider.context,
                        adUnitId,
                        request,
                        object : InterstitialAdLoadCallback() {
                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                                loadedInterstitialAds[adUnitId] = interstitialAd
                                if (continuation.isActive) {
                                    continuation.resume(Result.success(true))
                                }
                            }

                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                if (continuation.isActive) {
                                    continuation.resume(Result.failure(Exception("Failed to load interstitial ad: ${loadAdError.message}")))
                                }
                            }
                        })
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun showInterstitialAd(activity: Activity, adUnitId: String): Result<Boolean> =
        withContext(Dispatchers.Main) {
            try {
                val interstitialAd = loadedInterstitialAds[adUnitId]
                    ?: return@withContext Result.failure(IllegalStateException("No interstitial ad loaded for $adUnitId"))

                suspendCancellableCoroutine { continuation ->
                    interstitialAd.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                // Ad was dismissed, clean up and prepare for the next one
                                loadedInterstitialAds.remove(adUnitId)
                                if (continuation.isActive) {
                                    continuation.resume(Result.success(true))
                                }
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Ad failed to show
                                loadedInterstitialAds.remove(adUnitId)
                                if (continuation.isActive) {
                                    continuation.resume(Result.failure(Exception("Failed to show interstitial ad: ${adError.message}")))
                                }
                            }
                        }

                    interstitialAd.show(activity)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override fun isInterstitialAdLoaded(adUnitId: String): Boolean {
        return loadedInterstitialAds.containsKey(adUnitId)
    }
}
