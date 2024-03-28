package com.example.marvelapp.utils

import java.security.MessageDigest

object HashUtils {
    fun md5(input: String): String {
        return MessageDigest.getInstance("MD5").digest(input.toByteArray()).joinToString("") {
            "%02x".format(it)
        }
    }
}
