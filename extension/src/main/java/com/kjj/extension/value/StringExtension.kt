package com.kjj.extension.value

/**
 * Parses byte log tokens (0xNN), separated optionally by ASCII space, tab, CR or LF.
 * @throws IllegalArgumentException if any token or separator is malformed.
 */
fun String.parseByteHexLog(): ByteArray {
    val bytes = ByteArray(length / 4)
    var count = 0
    var index = 0
    fun hexDigit(char: Char): Int = when (char) {
        in '0'..'9' -> char - '0'
        in 'a'..'f' -> char - 'a' + 10
        in 'A'..'F' -> char - 'A' + 10
        else -> -1
    }
    while (index < length) {
        when (this[index]) {
            ' ', '\t', '\r', '\n' -> {
                index++
                continue
            }
        }
        require(length - index >= 4 && this[index] == '0' &&
            (this[index + 1] == 'x' || this[index + 1] == 'X')) {
            "Invalid byte hex token at index $index"
        }
        val high = hexDigit(this[index + 2])
        val low = hexDigit(this[index + 3])
        require(high >= 0 && low >= 0) { "Invalid hex digits at index $index" }
        bytes[count++] = ((high shl 4) or low).toByte()
        index += 4
    }
    return bytes.copyOf(count)
}
