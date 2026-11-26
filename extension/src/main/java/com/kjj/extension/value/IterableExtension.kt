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
