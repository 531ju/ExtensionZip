package com.kjj.extension.value

import org.junit.Assert.assertEquals
import org.junit.Test

class IterableExtensionTest {
    @Test fun emptyAndUniqueInputs() {
        assertEquals(emptySet<Int>(), emptyList<Int>().duplicateKeysBy { it })
        assertEquals(emptySet<Int>(), listOf(1, 2, 3).duplicateKeysBy { it })
    }

    @Test fun returnsKeysOnceInSecondOccurrenceOrder() {
        assertEquals(listOf("b", "a"),
            listOf("a", "b", "b", "a", "a").duplicateKeysBy { it }.toList())
    }

    @Test fun selectsKeysAndSupportsNull() {
        assertEquals(setOf(1, null), listOf("a", null, "b", null)
            .duplicateKeysBy { it?.length })
    }

    @Test fun traversesOnceAndCallsSelectorOncePerElement() {
        var traversals = 0
        var calls = 0
        val input = Iterable {
            check(++traversals == 1)
            listOf(1, 2, 1).iterator()
        }
        assertEquals(setOf(1), input.duplicateKeysBy { calls++; it })
        assertEquals(3, calls)
    }
}
