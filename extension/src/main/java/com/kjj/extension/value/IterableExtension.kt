package com.kjj.extension.value

/** Returns repeated keys once, in the order in which their second occurrence is encountered. */
fun <T, K> Iterable<T>.duplicateKeysBy(selector: (T) -> K): Set<K> {
    val seen = HashSet<K>()
    val duplicates = LinkedHashSet<K>()
    for (element in this) {
        val key = selector(element)
        if (!seen.add(key)) duplicates.add(key)
    }
    return duplicates
}

/**
 * Returns a map in input order, throwing [IllegalArgumentException] at the first duplicate key.
 * Supports null keys and values; calls [keySelector] once per visited element in a single traversal.
 * Unlike [duplicateKeysBy], stops immediately instead of collecting all duplicate keys.
 */
fun <T, K> Iterable<T>.associateByUnique(keySelector: (T) -> K): Map<K, T> {
    val result = LinkedHashMap<K, T>()
    for (element in this) {
        val key = keySelector(element)
        require(!result.containsKey(key)) { "Duplicate key encountered." }
        result[key] = element
    }
    return result
}
