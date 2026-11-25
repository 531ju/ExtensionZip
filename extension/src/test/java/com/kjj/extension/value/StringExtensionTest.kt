package com.kjj.extension.value

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.fail
import org.junit.Test

class StringExtensionTest {
    @Test fun serializersRoundTripEveryByte() {
        val bytes = ByteArray(256) { it.toByte() }
        assertArrayEquals(bytes, bytes.convertBytesToHex().parseByteHexLog())
        assertArrayEquals(bytes, bytes.convertToBytesToString().parseByteHexLog())
    }

    @Test fun acceptsMixedCaseWhitespaceAndAdjacentTokens() {
        assertArrayEquals(byteArrayOf(0, 0xab.toByte(), 0xcd.toByte(), -1),
            " \t0x000XAb\r\n0xcD 0XFF\t ".parseByteHexLog())
        val packet = byteArrayOf(0x01, 0x0a)
        assertArrayEquals(packet, "0x010x0a".parseByteHexLog())
        assertArrayEquals(packet, "0x01 0x0a ".parseByteHexLog())
    }

    @Test fun emptyAndAsciiWhitespaceProduceEmptyArray() {
        for (text in listOf("", " \t\r\n")) {
            assertArrayEquals(byteArrayOf(), text.parseByteHexLog())
        }
    }

    @Test fun rejectsMalformedTokensAndNonAsciiCharacters() {
        for (text in listOf("01", "0", "0x", "0x1", "0x123", "0x000",
            "0xgg", "0y01", "0 x01", "0x 01", "0x01,0x02", "0x01x",
            "0x01 0x", "0x01\u00a0", "\u20030x01", "\u000b0x01",
            "0x０１", "0x١٢", "0xＦＦ", "0x01\u0000")) {
            try {
                text.parseByteHexLog()
                fail("Accepted malformed input: $text")
            } catch (_: IllegalArgumentException) {
                // Expected strict grammar rejection.
            }
        }
    }
}
