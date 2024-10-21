package com.app.casino.retrofit

interface ProgressListener {
    fun update(bytesRead: Long, contentLength: Long, done: Boolean)
}
