package com.eddy.jedymovieapptest.app

import com.eddy.jedymovieapptest.data.di.dataModule
import com.eddy.jedymovieapptest.domain.di.domainModule
import com.eddy.jedymovieapptest.presentation.di.presentationModule
import com.eddy.sdk.ads.AdApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : AdApplication() {
    override fun onCreate() {
        startKoin {
            androidContext(androidContext = this@App)
            modules(dataModule, domainModule, presentationModule)
        }
        
        super.onCreate()
    }
}
