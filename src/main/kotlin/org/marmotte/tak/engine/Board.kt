package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import java.awt.BasicStroke
import java.awt.Graphics2D

class Board(val size: Int) : Drawable {

    companion object {
        fun startingPieces(player: Boolean): List<Piece> = mutableListOf<Piece>().also {
            for (i in 0..10) {
                it.add(Road(player))
            }
            it.add(CapStone(player))
        }
    }

    var activePlayer: Boolean = true
    val whiteReserve: List<Piece> = startingPieces(true)
    val blackReserve: List<Piece> = startingPieces(false)

    private val board: List<List<Tower>> = List(size) { x ->
        List(size) { y ->
            Tower(Pos(x, y))
        }
    }
    val status: GameStatus = GameStatus.ACTIVE // TODO

    fun reserveOf(player: Boolean): List<Piece> = if (player) whiteReserve else blackReserve

    fun generateLegalMovesFrom(selected: Pos): List<Move> {
        return emptyList() // TODO
    }

    fun pieceAt(tileNum: Pos): Tower {
        return board[tileNum.x][tileNum.y]
    }

    fun execute(move: Move): MoveOutcome {
        TODO()
    }

    fun resign(): Move {
        TODO()
    }

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        g.stroke = BasicStroke(1f)
        for (x in 0 until size) {
            for (y in 0 until size) {
                val tileNum = Pos(x, y)
                pieceAt(tileNum).draw(g, updateContext)
            }
        }
    }

}
