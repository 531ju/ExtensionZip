package com.kjj.extension.value

/**
 * Returns a new list with the element at [fromIndex] moved to [toIndex].
 * Both indices refer to positions in the original list; [toIndex] is the final position.
 * The receiver is not modified, including when both indices are equal.
 * @throws IndexOutOfBoundsException if either index is outside [List.indices].
 */
fun <T> List<T>.moved(fromIndex: Int, toIndex: Int): List<T> {
    if (fromIndex !in indices || toIndex !in indices) {
        throw IndexOutOfBoundsException("fromIndex=$fromIndex, toIndex=$toIndex, size=$size")
    }
    return toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
}
