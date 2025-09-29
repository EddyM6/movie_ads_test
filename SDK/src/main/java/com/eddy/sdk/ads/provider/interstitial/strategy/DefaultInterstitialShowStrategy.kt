package com.eddy.sdk.ads.provider.interstitial.strategy

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.eddy.sdk.ads.data.local.AppOpenLocalDataSource
import com.eddy.sdk.ads.data.local.AppOpenLocalDataSourceImpl

internal class DefaultInterstitialShowStrategy(
    private val appOpenLocalDataSource: AppOpenLocalDataSource
) : InterstitialShowStrategy {
    override suspend fun shouldShow(): Boolean {
        val appOpenCount = appOpenLocalDataSource.incrementAppOpenCound()
        return (appOpenCount % 3 == 0).also { if (appOpenCount >= LIMIT) reset() }
    }

    override suspend fun reset() {
        appOpenLocalDataSource.resetAppOpenCount()
    }

    companion object {
        private const val LIMIT = 16
        private const val APP_OPEN_COUNT_PREF = "app_open_count_pref"
        fun getDefaultStrategy(application: Application) = DefaultInterstitialShowStrategy(
            appOpenLocalDataSource = AppOpenLocalDataSourceImpl(
                application.getSharedPreferences(APP_OPEN_COUNT_PREF, MODE_PRIVATE)
            )
        )
    }
}
