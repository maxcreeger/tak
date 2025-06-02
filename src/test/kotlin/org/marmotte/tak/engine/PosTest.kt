package org.marmotte.tak.engine

import kotlin.test.Test
import kotlin.test.assertEquals

class PosTest {

    @Test
    fun testFiles() {
        assertEquals('A', Pos.file(0))
        assertEquals('D', Pos.file(3))
    }

    @Test
    fun testFileAndRow() {
        val pos = Pos('B', 3)
        assertEquals('B', pos.file)
        assertEquals(3, pos.row)
    }

    @Test
    fun testFileIndex() {
        val pos = Pos('B', 3)
        assertEquals(1, pos.fileIndex())
    }

    @Test
    fun testMove() {
        val pos = Pos('C', 3)
        assertEquals(Pos('C', 1), pos.move(Dir.NORTH, 2))
        assertEquals(Pos('C', 6), pos.move(Dir.SOUTH, 3))
        assertEquals(Pos('D', 3), pos.move(Dir.EAST, 1))
        assertEquals(Pos('A', 3), pos.move(Dir.WEST, 2))
    }

    @Test
    fun testDirTo() {
        val pos = Pos('C', 3)
        val north = Pos('C', 2)
        val south = Pos('C', 4)
        val east = Pos('D', 3)
        val west = Pos('B', 3)
        assertEquals(Dir.NORTH, pos.dirTo(north))
        assertEquals(Dir.SOUTH, pos.dirTo(south))
        assertEquals(Dir.EAST, pos.dirTo(east))
        assertEquals(Dir.WEST, pos.dirTo(west))
    }

    @Test
    fun testDistTo() {
        val pos = Pos('C', 3)
        val north = Pos('C', 2)
        val south = Pos('C', 6)
        val east = Pos('D', 3)
        val west = Pos('A', 3)
        assertEquals(1, pos.distTo(north, Dir.NORTH))
        assertEquals(3, pos.distTo(south, Dir.SOUTH))
        assertEquals(1, pos.distTo(east, Dir.EAST))
        assertEquals(2, pos.distTo(west, Dir.WEST))
    }
}
