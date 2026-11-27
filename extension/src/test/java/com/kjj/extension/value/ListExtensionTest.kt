package com.kjj.extension.value

import org.junit.Assert.*
import org.junit.Test

class ListExtensionTest {
    @Test fun movesForwardAndBackward() {
        val input = listOf("a", "b", "c", "d")
        assertEquals(listOf("b", "c", "d", "a"), input.moved(0, 3))
        assertEquals(listOf("d", "a", "b", "c"), input.moved(3, 0))
        assertEquals(listOf("a", "c", "b", "d"), input.moved(1, 2))
    }

    @Test fun preservesReceiverAndCopiesEvenForSameIndex() {
        val input = mutableListOf(1, 2, 3)
        val result = input.moved(1, 1)
        assertEquals(listOf(1, 2, 3), result)
        assertNotSame(input, result)
        input[0] = 99
        assertEquals(listOf(1, 2, 3), result)
    }

    @Test fun supportsNullDuplicatesAndSingleton() {
        assertEquals(listOf(null, "a", "a"), listOf("a", null, "a").moved(0, 2))
        assertEquals(listOf(42), listOf(42).moved(0, 0))
    }

    @Test fun rejectsInvalidIndicesIncludingEmptyList() {
        for ((from, to) in listOf(-1 to 0, 0 to -1, 3 to 0, 0 to 3, Int.MIN_VALUE to Int.MAX_VALUE)) {
            assertThrows(IndexOutOfBoundsException::class.java) { listOf(1, 2, 3).moved(from, to) }
        }
        assertThrows(IndexOutOfBoundsException::class.java) { emptyList<Int>().moved(0, 0) }
    }

    @Test fun everyValidPairHasExpectedFinalPositionAndOrder() {
        val input = (0..5).toList()
        for (from in input.indices) for (to in input.indices) {
            val result = input.moved(from, to)
            assertEquals(input[from], result[to])
            assertEquals(input.filterIndexed { index, _ -> index != from },
                result.filterIndexed { index, _ -> index != to })
            assertEquals(input.size, result.size)
        }
        assertEquals((0..5).toList(), input)
    }
}
