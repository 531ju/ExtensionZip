package com.kjj.extension.value

import java.nio.ByteOrder
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ByteArrayExtensionTest {
    @Test fun everyUnsignedValueInBothOrdersLeavesInputUnchanged() {
        for (value in 0..65535) {
            val little = byteArrayOf(value.toByte(), (value ushr 8).toByte())
            val big = little.reversedArray()
            val littleBefore = little.copyOf()
            val bigBefore = big.copyOf()
            assertEquals(value, little.readUInt16())
            assertEquals(value, little.readUInt16(byteOrder = ByteOrder.LITTLE_ENDIAN))
            assertEquals(value, big.readUInt16(byteOrder = ByteOrder.BIG_ENDIAN))
            assertArrayEquals(littleBefore, little)
            assertArrayEquals(bigBefore, big)
        }
    }

    @Test fun nonzeroAndLastOffsetsAndParserIntegration() {
        val packet = "0x99 0x34 0x12 0xff 0xff".parseByteHexLog()
        val before = packet.copyOf()
        assertEquals(4660, packet.readUInt16(1))
        assertEquals(13330, packet.readUInt16(1, ByteOrder.BIG_ENDIAN))
        assertEquals(65535, packet.readUInt16(3))
        assertEquals(65535, packet.readUInt16(3, ByteOrder.BIG_ENDIAN))
        assertArrayEquals(before, packet)
    }

    @Test fun rejectsShortArraysAndInvalidOffsetsWithoutMutation() {
        for (size in 0..4) {
            val bytes = ByteArray(size) { it.toByte() }
            val before = bytes.copyOf()
            for (offset in listOf(Int.MIN_VALUE, -1, 0, size - 1, size, Int.MAX_VALUE)) {
                if (offset >= 0 && offset <= size - 2) continue
                for (order in listOf(ByteOrder.LITTLE_ENDIAN, ByteOrder.BIG_ENDIAN)) {
                    try {
                        bytes.readUInt16(offset, order)
                        fail("Accepted size=$size offset=$offset")
                    } catch (_: IndexOutOfBoundsException) {
                        // Expected, including extreme offsets without arithmetic overflow.
                    }
                    assertArrayEquals(before, bytes)
                }
            }
        }
    }
}
