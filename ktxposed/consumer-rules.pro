-keep class com.houvven.ktxposed.HookStatus$Companion {
    private <methods>;
}
-keep class * implements com.houvven.ktxposed.service.XposedHookLoadPackage {
    handleLoadPackage();
}