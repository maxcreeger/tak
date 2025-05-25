package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import java.awt.BasicStroke
import java.awt.Graphics2D

class Board(val size: Int) : Drawable {

    var activePlayer: Boolean = true
    val whiteReserve: Reserve = Reserve(true)
    val blackReserve: Reserve = Reserve(false)

    private val board: List<List<Tower>> = List(size) { x ->
        List(size) { y ->
            Tower(Pos(x, y))
        }
    }
    val status: GameStatus = GameStatus.ACTIVE // TODO

    fun reserveOf(player: Boolean): Reserve = if (player) whiteReserve else blackReserve

    fun generateLegalMovesFrom(selected: Pos): List<Move> {
        return emptyList() // TODO
    }

    fun pieceAt(pos: Pos): Tower? {
        return board.getOrNull(pos.x)?.getOrNull(pos.y)
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
                pieceAt(tileNum)?.draw(g, updateContext)
            }
        }
    }

}
