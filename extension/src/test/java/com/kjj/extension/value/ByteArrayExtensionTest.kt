package com.kjj.extension.value

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class ByteArrayExtensionTest {
    @Test
    fun knownVectors() {
        assertEquals(0xCBF43926L, "123456789".toByteArray(Charsets.US_ASCII).crc32())
        assertEquals(0x3610A686L, "hello".toByteArray(Charsets.US_ASCII).crc32())
        assertEquals(0xFF000000L, byteArrayOf(0xFF.toByte()).crc32())
        assertEquals(0L, byteArrayOf().crc32())
    }

    @Test
    fun preservesInputAndIsolatesCalls() {
        val input = "123456789".toByteArray(Charsets.US_ASCII)
        val original = input.copyOf()
        repeat(3) {
            assertEquals(0xCBF43926L, input.crc32())
            assertEquals(0x3610A686L, "hello".toByteArray(Charsets.US_ASCII).crc32())
            assertEquals(0L, byteArrayOf().crc32())
        }
        assertArrayEquals(original, input)
    }

    @Test
    fun checksParsedPacketLogs() {
        assertEquals(0xCBF43926L, "0x310x320x330x340x350x360x370x380x39".parseByteHexLog().crc32())
        assertEquals(0x3610A686L, "0x68 0x65 0x6c 0x6c 0x6f ".parseByteHexLog().crc32())
    }
}
