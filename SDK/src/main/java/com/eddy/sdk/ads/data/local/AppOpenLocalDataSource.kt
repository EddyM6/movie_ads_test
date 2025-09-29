package com.eddy.sdk.ads.data.local

interface AppOpenLocalDataSource {
    suspend fun incrementAppOpenCound(): Int
    suspend fun resetAppOpenCount()
    suspend fun getAppOpenCount(): Int
}
