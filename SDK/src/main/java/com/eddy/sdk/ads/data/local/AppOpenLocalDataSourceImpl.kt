package com.eddy.sdk.ads.data.local

import android.content.SharedPreferences

internal class AppOpenLocalDataSourceImpl(
    private val preferences: SharedPreferences
) : AppOpenLocalDataSource {
    private val editor: SharedPreferences.Editor by lazy {
        preferences.edit()
    }

    override suspend fun incrementAppOpenCound(): Int {
        var count = preferences.getInt(APP_OPEN_COUNT, 0)
        editor.putInt(APP_OPEN_COUNT, ++count).apply()
        return count
    }

    override suspend fun resetAppOpenCount() {
        editor.putInt(APP_OPEN_COUNT, 0).apply()
    }

    override suspend fun getAppOpenCount(): Int {
        return preferences.getInt(APP_OPEN_COUNT, 0)
    }

    companion object {
        const val APP_OPEN_COUNT = "app_open_count"
    }
}
