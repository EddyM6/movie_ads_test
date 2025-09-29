# AdMob SDK Integration

This module provides a comprehensive solution for integrating AdMob ads into your Android application, specifically designed to work as a library with a clean abstraction layer for future expansion to multiple ad providers.

## Features

- Native ads with Jetpack Compose UI components
- Interstitial ads with automatic display logic
- App open event tracking
- Dependency injection with Koin
- Abstraction layer for future ad provider integrations
- Test mode support
- Provider type selection to allow switching between ad providers
- Builder pattern for easy configuration

## Setup

### 1. Dependencies

The SDK module already includes the necessary dependencies. Make sure your app module depends on this SDK module.

### 2. Update Your Application Class

Make your Application class inherit from `AdApplication`:

```kotlin
import com.eddy.sdk.ads.AdApplication
import com.eddy.sdk.ads.config.AdConfig
import com.eddy.sdk.ads.config.AdProviderType
import android.app.Activity
import java.lang.ref.WeakReference

class YourApp : AdApplication() {
    private var currentActivity: WeakReference<Activity> = WeakReference(null)
    
    override fun onCreate() {
        // Track the current activity
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            // Implement the necessary methods
            override fun onActivityResumed(activity: Activity) {
                currentActivity = WeakReference(activity)
            }
            // Implement other callbacks as needed
        })
        
        // Initialize your other systems first if needed
        
        // Always call super.onCreate() to initialize the ad system
        super.onCreate()
        
        // Optionally configure the AdsSdk with custom settings using the builder pattern
        AdsSdk.initialize(
            application = this,
            config = AdConfig.builder(this)
                .setProviderType(AdProviderType.AD_MOB)
                .setTestMode(BuildConfig.DEBUG) // Use test mode for debug builds
                .setAdMobAppId("your-admob-app-id")
                .setNativeAdUnitId("your-native-ad-unit-id")
                .setInterstitialAdUnitId("your-interstitial-ad-unit-id")
                .build()
        )
    }
    
    /**
     * Required implementation for the abstract method in AdApplication
     */
    override fun getCurrentActivity(): Activity? {
        return currentActivity.get()
    }
}
```

### 3. Update Your AndroidManifest.xml

```xml
<application
    android:name=".YourApp"
    ...>
    
    <!-- AdMob App ID -->
    <meta-data
        android:name="com.google.android.gms.ads.APPLICATION_ID"
        android:value="your-admob-app-id" />
        
</application>
```

## Using Native Ads

### 1. In Jetpack Compose UI

```kotlin
@Composable
fun YourScreen() {
    // Get the native ad composable from the AdsSdk
    val NativeAdComposable = AdsSdk.getNativeAdComposable()
    
    Column {
        // Your content
        
        // Display the ad where needed
        NativeAdComposable(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        
        // More content
    }
}
```

## Interstitial Ads

Interstitial ads are automatically managed by the SDK. They will be shown every second time the app is resumed (when the user returns to the app).

No additional code is required for this functionality, as it's handled by the `AdApplication` class.

## Advanced Configuration

### Custom Ad Configuration

```kotlin
// In your Application class
AdsSdk.initialize(
    application = this,
    config = AdConfig.builder(this)
        .setProviderType(AdProviderType.AD_MOB)
        .setTestMode(BuildConfig.DEBUG)
        .setAdMobAppId("your-admob-app-id")
        .setNativeAdUnitId("your-native-ad-unit-id")
        .setInterstitialAdUnitId("your-interstitial-ad-unit-id")
        .build()
)
```

### Dynamic Configuration Updates

```kotlin
// Update configuration at runtime
AdsSdk.updateConfig(context) { builder ->
    // Modify any configuration properties
    builder.setTestMode(true)
}
```

## Architecture

The SDK follows SOLID principles and is designed for extensibility:

- `AdsSdk`: Main entry point singleton
- `AdApplication`: Base Application class that handles lifecycle events
- `AdProvider` interfaces: Abstractions for ad functionality
- Implementation classes: Concrete implementations for AdMob
- UI Components: Jetpack Compose wrappers for native ads

## Adding New Ad Providers

To add a new ad provider:

1. Create a new implementation of `NativeAdProvider` and `InterstitialAdProvider`
2. Update the Koin module in `AdModules.kt` to inject the new provider
3. No changes to the app code are required

## Testing

For testing, the SDK uses AdMob's test ad unit IDs by default when in test mode.

## Troubleshooting

- If ads aren't showing, check if test mode is enabled correctly.
- Verify that the correct ad unit IDs are provided.
- Ensure that the app has Internet permissions.
- For native ads, ensure the composable is given enough space to render.
