package org.marmotte.tak.engine

import org.marmotte.tak.engine.Dir.*
import kotlin.math.abs
import kotlin.math.max

/** Position on the Board. Coordinates from `0..n` **/
data class Pos(
    val file: Int,
    val row: Int
) {
    constructor(file: Char, row: Int): this(file.lowercaseChar() - 'A', row)
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

    fun distTo(other: Pos, dir: Dir): Int {
        return when (dir) {
            NORTH -> if(file == other.file) max(other.row - row, 0) else 0
            SOUTH -> if(file == other.file) max(row - other.row, 0) else 0
            EAST -> if(row == other.row) max(file - other.file, 0) else 0
            WEST -> if(row == other.row) max(other.file - file, 0) else 0
        }
    }
}
