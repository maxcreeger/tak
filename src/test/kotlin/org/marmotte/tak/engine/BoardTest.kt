package org.marmotte.tak.engine

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BoardTest {

    @Test
    fun testNewGame() {
        val newGame = Board.newGame(5)
        assertTrue(newGame.activePlayer)
        assertEquals(5, newGame.size)
        assertTrue { newGame.towers.all { it.isEmpty() } }
        assertEquals(1, newGame.blackReserve.capstones.size)
        assertEquals(21, newGame.whiteReserve.tiles.size)
    }

    @Test
    fun testStatusActive() {
        val newGame = Board.newGame(5)
        assertEquals(GameStatus.ACTIVE, newGame.status)
    }

    @Test
    fun testStatusEmptyWhiteReserve() {
        val size = 5
        val towers = List(size) { row ->
            List(size) { fileNum ->
                if (fileNum % 2 == 0)
                    if (row <= 2)
                        Tower(Pos(Pos.file(fileNum), row), listOf(Road(true)))
                    else
                        Tower(Pos(Pos.file(fileNum), row), listOf(Road(false)))
                else
                    Tower(Pos(Pos.file(fileNum), row), emptyList())
            }
        }
        val board = Board(5, true, towers, 0, 21, 0, 1, 0)
        // Test
        assertEquals(GameStatus.WHITE_WIN, board.status)
    }

    @Test
    fun testStatusBoardFull() {
        val size = 5
        val towers = List(size) { row ->
            List(size) { fileNum ->
                if ((fileNum + row) % 2 == 0)
                    Tower(Pos(Pos.file(fileNum), row), listOf(Road(true)))
                else
                    Tower(Pos(Pos.file(fileNum), row), listOf(Road(false)))
            }
        }
        val board = Board(5, true, towers, 21, 21, 1, 1, 0)
        // Test
        assertEquals(GameStatus.WHITE_WIN, board.status)
    }

    @Test
    fun testStatusBoardFullOfCapStone() {
        val size = 5
        val towers = List(size) { row ->
            List(size) { fileNum ->
                if ((fileNum + row) % 2 == 0)
                    Tower(Pos(Pos.file(fileNum), row), listOf(CapStone(true)))
                else
                    Tower(Pos(Pos.file(fileNum), row), listOf(Road(false)))
            }
        }
        val board = Board(5, true, towers, 21, 21, 1, 1, 0)
        // Test
        assertEquals(GameStatus.BLACK_WIN, board.status)
    }

    @Test
    fun testStatusBoardFullDraw() {
        val size = 5
        val towers = List(size) { row ->
            List(size) { fileNum ->
                if ((fileNum + row) % 2 == 0) {
                    val piece = if (fileNum == 0 && row == 0) CapStone(true) else Road(true)
                    Tower(Pos(Pos.file(fileNum), row), listOf(piece))
                } else {
                    Tower(Pos(Pos.file(fileNum), row), listOf(Road(false)))
                }
            }
        }
        val board = Board(5, true, towers, 21, 21, 1, 1, 0)
        // Test
        assertEquals(GameStatus.DRAW, board.status)
    }

    @Test
    fun testConnectivity() {
        val size = 5
        val towers = List(size) { row ->
            List(size) { fileNum ->
                if (fileNum == 0)
                    Tower(Pos(Pos.file(fileNum), row), listOf(Road(true)))
                else if ((fileNum + row) % 2 == 0) {
                    Tower(Pos(Pos.file(fileNum), row), emptyList())
                } else {
                    Tower(Pos(Pos.file(fileNum), row), listOf(Road(false)))
                }
            }
        }
        val board = Board(5, true, towers, 21, 21, 1, 1, 0)
        // Test
        assertEquals(GameStatus.WHITE_WIN, board.status)
    }
}
