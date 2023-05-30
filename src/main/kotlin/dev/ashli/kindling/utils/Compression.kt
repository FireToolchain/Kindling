package dev.ashli.kindling.utils

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * Takes a string and GZIPs it, then Base64 encodes it.
 */
fun encode(content: String): String {
    val bos = ByteArrayOutputStream()
    GZIPOutputStream(bos).bufferedWriter(UTF_8).use { it.write(content) }
    return Base64.getEncoder().encodeToString(bos.toByteArray())
}

/**
 * Takes a string and Base64 unencodes it, then un-GZIPs it.
 */
fun unencode(content: String): String =
    GZIPInputStream(Base64.getDecoder().decode(content).inputStream()).bufferedReader(UTF_8).use { it.readText() }