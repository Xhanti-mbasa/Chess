# Add project specific ProGuard rules here.
# Keep Retrofit and Moshi models for serialization.
-keep class com.example.mt5monitor.** { *; }
-keep class com.squareup.moshi.** { *; }
-dontwarn okhttp3.**
