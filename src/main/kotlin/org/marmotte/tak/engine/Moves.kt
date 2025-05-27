package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import java.awt.Graphics2D

interface Move : Drawable {
    fun applyTo(board: Board): MoveOutcome

}

data class StackMove(val player: Boolean, private val stack: Stack, val pos: Pos, val dir: Dir) : Move {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        TODO("Not yet implemented")
    }

    override fun applyTo(board: Board): MoveOutcome {
        TODO("Not yet implemented")
    }
}

data class PlaceReserveWall(val player: Boolean, val pos: Pos) : Move {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        TODO("Not yet implemented")
    }

    override fun applyTo(board: Board): MoveOutcome {
        TODO("Not yet implemented")
    }
}

data class PlaceReserveRoad(val player: Boolean, val pos: Pos) : Move {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        TODO("Not yet implemented")
    }

    override fun applyTo(board: Board): MoveOutcome {
        val tower = board.towerAt(pos) ?: return MoveOutcome.illegal(board, this, "Outside board range: $pos, board size is ${board.size}")
        if (tower.pieces.isNotEmpty()) return MoveOutcome.illegal(board, this, "Board has a non-empty tower at $pos, cannot put a Tile there from the reserve")

        val newBoard = board.change(
            mapOf(
                pos to mapOf(tower.pieces.size to Road(player))
            ),
            consumedTile = true
        )
        return MoveOutcome(board, this, newBoard)
    }
}

data class PlaceCapStone(val player: Boolean, val capStone: CapStone, val pos: Pos) : Move {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        TODO("Not yet implemented")
    }

    override fun applyTo(board: Board): MoveOutcome {
        val tower = board.towerAt(pos) ?: return MoveOutcome.illegal(board, this, "Outside board range: $pos, board size is ${board.size}")
        if (tower.pieces.isNotEmpty()) return MoveOutcome.illegal(board, this, "Board has a non-empty tower at $pos, cannot put a CapStone there from the reserve")
        val newBoard = board.change(
            mapOf(
                pos to mapOf(tower.pieces.size to capStone)
            ),
            consumedCapStone = true
        )
        return MoveOutcome(board, this, newBoard)
    }
}
