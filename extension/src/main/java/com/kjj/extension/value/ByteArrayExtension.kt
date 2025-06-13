package com.kjj.extension.value

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