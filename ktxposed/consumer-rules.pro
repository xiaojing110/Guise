-keep class com.houvven.ktxposed.HookStatus* {
    private <methods>;
}

-keep class com.houvven.ktxposed.HookStatus$Companion {
    private <methods>;
}
-keep class * implements com.houvven.ktxposed.hook.method.service.HookLoadPackageEntrance {
    handleLoadPackage();
}