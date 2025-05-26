package org.marmotte.tak.engine

data class Pos(
    val file: Int,
    val row: Int
) {
    fun fileLetter(): Char = 'A' + file
}
