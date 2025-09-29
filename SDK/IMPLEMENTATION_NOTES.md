# SDK Implementation Notes

## Recent Changes

### Activity Tracking Improvement

- Removed the abstract `getCurrentActivity()` method from `AdApplication`
- Implemented activity tracking directly in the SDK using `WeakReference<Activity>`
- This simplifies integration for app modules as they no longer need to implement this method
- Added proper activity lifecycle tracking in the SDK's base class

### Code Organization

- The `NativeAd` class has been moved from `com.eddy.sdk.ads.model` to `com.eddy.sdk.ads.provider.native.model` for better organization
- Updated all imports across the codebase to reference the new location

### Naming Updates

- Changed `AdProviderType.AD_MOB` to `AdProviderType.AdMob` for better Kotlin naming conventions

### Bug Fixes

- Fixed recursive call to `onCreate()` in the app's `App` class
- Fixed activity reference handling to avoid potential memory leaks

## Integration Notes

When integrating this SDK, remember to:

1. Extend `AdApplication` in your app's `Application` class
2. Call `super.onCreate()` after initializing your app's dependencies
3. Configure the `AdsSdk` with your specific settings if needed

Example:

```kotlin
class App : AdApplication() {
    override fun onCreate() {
        // Initialize your dependencies first
        startKoin {
            androidContext(this@App)
            modules(yourModules)
        }
        
        // Call super.onCreate() to initialize AdsSdk with base functionality
        super.onCreate()
        
        // Optionally configure with custom settings
        AdsSdk.initialize(
            application = this,
            config = AdConfig.builder(this)
                .setProviderType(AdProviderType.AdMob)
                .setTestMode(BuildConfig.DEBUG)
                .setAdMobAppId("your-admob-app-id")
                .build()
        )
    }
}
```

Note that you no longer need to track the current activity or implement `getCurrentActivity()`; this is now handled automatically by the SDK.
