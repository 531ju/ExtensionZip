package com.kjj.extension.value

import java.nio.ByteBuffer
import java.nio.ByteOrder

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

/** Reads two bytes as an unsigned integer without modifying this array. */
fun ByteArray.readUInt16(offset: Int = 0, byteOrder: ByteOrder = ByteOrder.LITTLE_ENDIAN): Int {
    if (offset < 0 || offset > size - 2) {
        throw IndexOutOfBoundsException("Two bytes required at offset $offset (size $size)")
    }
    return ByteBuffer.wrap(this).order(byteOrder).getShort(offset).toInt() and 0xffff
}
