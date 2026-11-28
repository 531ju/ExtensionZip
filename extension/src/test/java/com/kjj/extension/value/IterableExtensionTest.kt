package com.kjj.extension.value

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class IterableExtensionTest {
    @Test fun associatesUniqueElementsInInputOrder() {
        assertEquals(emptyMap<Int, String>(), emptyList<String>().associateByUnique { it.length })
        val input = listOf("bbb", "a", "cc")
        val result = input.associateByUnique { it.length }
        assertEquals(listOf(3, 1, 2), result.keys.toList())
        assertEquals(input, result.values.toList())
    }

    @Test fun associatesNullKeysAndValues() {
        val result = listOf(null, "a").associateByUnique { it?.length }
        assertEquals(listOf(null, 1), result.keys.toList())
        assertEquals(listOf(null, "a"), result.values.toList())
        assertThrows(IllegalArgumentException::class.java) {
            listOf<String?>(null, null).associateByUnique { it?.length }
        }
        assertThrows(IllegalArgumentException::class.java) {
            listOf(null, "a").associateByUnique { 1 }
        }
        assertThrows(IllegalArgumentException::class.java) {
            listOf("a", "b").associateByUnique { null }
        }
    }

    @Test fun rejectsDuplicateKeysWithoutExposingThem() {
        val key = object {
            override fun toString(): String = error("Must not stringify a duplicate key")
        }
        val exception = assertThrows(IllegalArgumentException::class.java) {
            listOf(1, 2).associateByUnique { key }
        }
        assertEquals("Duplicate key encountered.", exception.message)
    }

    @Test fun stopsAtFirstDuplicateWithoutReadingFurther() {
        var reads = 0
        val selected = mutableListOf<String>()
        val input = Iterable {
            object : Iterator<String> {
                override fun hasNext(): Boolean {
                    check(reads < 3) { "Must stop immediately at the duplicate" }
                    return true
                }
                override fun next(): String = listOf("a", "bb", "c")[reads++]
            }
        }
        assertThrows(IllegalArgumentException::class.java) {
            input.associateByUnique { selected.add(it); it.length }
        }
        assertEquals(3, reads)
        assertEquals(listOf("a", "bb", "c"), selected)
    }

    @Test fun associatesInOneTraversalWithOneSelectionPerElement() {
        var traversals = 0
        val selected = mutableListOf<Int>()
        val input = Iterable {
            check(++traversals == 1)
            listOf(3, 1, 2).iterator()
        }
        assertEquals(mapOf(3 to 3, 1 to 1, 2 to 2),
            input.associateByUnique { selected.add(it); it })
        assertEquals(1, traversals)
        assertEquals(listOf(3, 1, 2), selected)
    }

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
