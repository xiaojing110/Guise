package com.houvven.twig.preset

import com.houvven.twig.preset.adapter.PresetAdapter

@Suppress("unused", "EnumEntryName", "SpellCheckingInspection")
enum class TimeZonePreset(
    override val label: String,
    override val value: String
) : PresetAdapter {
    Asia_Shanghai("Asia/Shanghai", "Asia/Shanghai"),
    Asia_Pacific("Asia/Pacific", "Asia/Pacific"),
    Asia_Tokyo("Asia/Tokyo", "Asia/Tokyo"),
    Asia_Vladivostok("Asia/Vladivostok", "Asia/Vladivostok"),
    Asia_Yakutsk("Asia/Yakutsk", "Asia/Yakutsk"),
    Australia_Adelaide("Australia/Adelaide", "Australia/Adelaide"),
    Australia_Brisbane("Australia/Brisbane", "Australia/Brisbane"),
    Australia_Broken_Hill("Australia/Broken_Hill", "Australia/Broken_Hill"),
    Australia_Currie("Australia/Currie", "Australia/Currie"),
    Australia_Darwin("Australia/Darwin", "Australia/Darwin"),
    Australia_Hobart("Australia/Hobart", "Australia/Hobart"),
    Australia_Melbourne("Australia/Melbourne", "Australia/Melbourne"),
    Australia_Perth("Australia/Perth", "Australia/Perth"),
    Australia_Sydney("Australia/Sydney", "Australia/Sydney"),
    Australia_Tasman("Australia/Tasman", "Australia/Tasman"),
    Australia_Victoria("Australia/Victoria", "Australia/Victoria"),
    Australia_West("Australia/West", "Australia/West"),
    Australia_Central("Australia/Central", "Australia/Central"),
    Australia_Eucla("Australia/Eucla", "Australia/Eucla"),
    Australia_LHI("Australia/LHI", "Australia/LHI"),
    Australia_Lord_Howe("Australia/Lord_Howe", "Australia/Lord_Howe"),
    Australia_North("Australia/North", "Australia/North"),
    Australia_South("Australia/South", "Australia/South"),
    Australia_Yancowinna("Australia/Yancowinna", "Australia/Yancowinna"),
    Europe_Amsterdam("Europe/Amsterdam", "Europe/Amsterdam"),
    Europe_Andorra("Europe/Andorra", "Europe/Andorra"),
    Europe_Astrakhan("Europe/Astrakhan", "Europe/Astrakhan"),
    Europe_Athens("Europe/Athens", "Europe/Athens"),
    Europe_Belgrade("Europe/Belgrade", "Europe/Belgrade"),
    Europe_Bratislava("Europe/Bratislava", "Europe/Bratislava"),
    Europe_Bucharest("Europe/Bucharest", "Europe/Bucharest"),
    Europe_Budapest("Europe/Budapest", "Europe/Budapest"),
    Europe_Chisinau("Europe/Chisinau", "Europe/Chisinau"),
    Europe_Copenhagen("Europe/Copenhagen", "Europe/Copenhagen"),
    Europe_Dublin("Europe/Dublin", "Europe/Dublin"),
    Europe_Gibraltar("Europe/Gibraltar", "Europe/Gibraltar"),
    Europe_Guernsey("Europe/Guernsey", "Europe/Guernsey"),
    Europe_Helsinki("Europe/Helsinki", "Europe/Helsinki"),
    Europe_Isle_of_Man("Europe/Isle_of_Man", "Europe/Isle_of_Man"),
    Europe_Istanbul("Europe/Istanbul", "Europe/Istanbul")
    ;
}