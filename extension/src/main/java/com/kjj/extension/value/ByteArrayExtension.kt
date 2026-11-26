package com.kjj.extension.value

import java.util.zip.CRC32

fun ByteArray.convertBytesToHex(): String {
    return StringBuilder().let {
        for (byte in this) {
            it.append(String.format("0x%02x", byte))
        }
        it
    }.toString()
}

fun ByteArray.convertToBytesToString() : String {
    return StringBuilder().let {
        for (byte in this) {
            it.append(String.format("0x%02x", byte)).append(" ")
        }
        it
    }.toString()
}

/** Returns the unsigned CRC-32/ISO-HDLC checksum without modifying this array. */
fun ByteArray.crc32(): Long = CRC32().apply { update(this@crc32) }.value
