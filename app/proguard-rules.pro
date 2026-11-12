-keep class * implements com.houvven.twig.config.TwigConfig {
    <fields>;
}

# 模板使用 kotlinx.serialization 持久化
-keep class com.houvven.twig.config.** { *; }
-keepclassmembers class com.houvven.twig.config.** { *; }