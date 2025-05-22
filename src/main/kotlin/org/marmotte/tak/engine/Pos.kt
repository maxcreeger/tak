package org.marmotte.tak.engine

data class Pos(
    val x: Int,
    val y: Int
) {
    fun fileLetter(): Char = 'A' + x
}
