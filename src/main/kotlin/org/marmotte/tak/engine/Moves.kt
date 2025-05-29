package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.engine.PieceType.CAPSTONE
import java.awt.Graphics2D

interface Move : Drawable {
    fun applyTo(board: Board): MoveOutcome

}

data class StackMove(val player: Boolean, private val stack: Stack, val pos: Pos, val dir: Dir) : Move {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        TODO("Not yet implemented")
    }

    override fun applyTo(board: Board): MoveOutcome {
        var crushedWall: Pair<Pos, Int>? = null
        val fallenTiles = stack
            .pieces
            .mapIndexed { height, piece ->
                val newPos = pos.move(dir, height + 1)
                val tower = board.towerAt(newPos) ?: return MoveOutcome.illegal(board, this, "Falling outside the board")
                when(tower.topPiece) {
                    null -> newPos to listOf(piece)
                    is CapStone -> return MoveOutcome.illegal(board, this, "Pieces falling onto a CapStone at $newPos")
                    is ReserveTile -> throw UnsupportedOperationException("There should never be a ReserveTile in a Stack")
                    is Road -> newPos to listOf(piece)
                    is Wall -> {
                        if(piece.piece == CAPSTONE) { // crushing the wall
                            crushedWall = newPos to tower.pieces.size - 1
                            newPos to listOf(piece)
                        } else {
                            return MoveOutcome.illegal(board, this, "Pieces falling onto a Wall at $newPos")
                        }
                    }
                }
            }
            .toMap()
        val originalTower = board.towerAt(pos)!!
        val depletedStack = mapOf(pos to originalTower.pieces.indexOf(stack.bottom))
        val removedTiles = if(crushedWall == null) {
            depletedStack
        }else {
            depletedStack + crushedWall
        }
        val newBoard = board.change(
            removedTiles,
            fallenTiles,
            consumedTile = true
        )
        return MoveOutcome(board, this, newBoard)
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
            emptyMap(),
            mapOf(pos to listOf(Road(player))),
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
            emptyMap(),
            mapOf(pos to listOf(capStone)),
            consumedCapStone = true
        )
        return MoveOutcome(board, this, newBoard)
    }
}
