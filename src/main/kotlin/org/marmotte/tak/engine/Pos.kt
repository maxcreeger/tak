package org.marmotte.tak.engine

import org.marmotte.tak.engine.Dir.*
import kotlin.math.abs

/** Position on the Board. Coordinates from `0..n` **/
data class Pos(
    val file: Int,
    val row: Int
) {
    fun fileLetter(): Char = 'A' + file
    fun move(dir: Dir, nb: Int = 1): Pos {
        return when (dir) {
            NORTH -> Pos(file, row - nb)
            SOUTH -> Pos(file, row + nb)
            EAST -> Pos(file + nb, row)
            WEST -> Pos(file - nb, row)
        }
    }

    fun dirTo(other: Pos): Dir {
        val northSouth = abs(other.file - file) < abs(other.row - row)
        val dir = if (northSouth) {
            if (row > other.row) NORTH else SOUTH
        } else {
            if (file > other.file) WEST else EAST
        }
        return dir
    }
}
