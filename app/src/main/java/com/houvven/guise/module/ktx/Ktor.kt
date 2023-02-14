package com.houvven.guise.module.ktx

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream


suspend fun HttpClient.download(url: String, fileOutputStream: FileOutputStream) {
    get<HttpResponse> { url(url) }.readBytes().let {
        withContext(Dispatchers.IO) {
            fileOutputStream.write(it)
            fileOutputStream.flush()
            fileOutputStream.close()
        }
    }
}
