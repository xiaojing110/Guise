-keep class * implements com.houvven.twig.config.TwigConfig {
    <fields>;
}

# 模板使用 kotlinx.serialization 持久化
-keep class com.houvven.twig.config.** { *; }
-keepclassmembers class com.houvven.twig.config.** { *; }

# LSPosed API 102 由框架运行时提供
-dontwarn io.github.libxposed.api.**
-keep class io.github.libxposed.api.** { *; }

# APK 内 META-INF/xposed/java_init.list 引用的入口类，不能被混淆
-keep class com.houvven.twig.xposed.MainHook { *; }