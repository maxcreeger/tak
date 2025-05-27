package org.marmotte.tak.engine

/** Position on the Board. Coordinates from `0..n` **/
data class Pos(
    val file: Int,
    val row: Int
) {
    fun fileLetter(): Char = 'A' + file
}
