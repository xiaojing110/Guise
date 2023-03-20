package com.houvven.twig.xposed.hook

import android.content.ContentResolver
import android.provider.ContactsContract
import android.provider.MediaStore
import com.houvven.ktxposed.hook.method.beforeHookAllMethods
import com.houvven.twig.xposed.adapter.TwigHookAdapter

class PrivacyRelated : TwigHookAdapter {
    override fun onHook() {
        listOf(
            config.BLANK_PASS_AUDIO to MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            config.BLANK_PASS_VIDEO to MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            config.BLANK_PASS_PHOTO to MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            config.BLANK_PASS_CONTACTS to ContactsContract.Contacts.CONTENT_URI
        ).forEach { (enable, uri) ->
            ContentResolver::class.beforeHookAllMethods("query") { param ->
                if (enable && param.args.any { it == uri }) {
                    param.result = null
                }
            }
        }
    }
}