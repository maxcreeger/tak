package org.marmotte.tak.engine

import org.marmotte.tak.engine.PieceType.CAPSTONE

interface Move {
    fun applyTo(board: Board): MoveOutcome

}

data class StackMove(val player: Boolean, private val stack: Stack, val pos: Pos, val dir: Dir, val distrib: List<Int>) : Move {

    override fun applyTo(board: Board): MoveOutcome {
        var crushedWall: Pair<Pos, Int>? = null
        if (distrib.sum() != stack.size) return MoveOutcome.illegal(board, this, "Bad distrib: stack has ${stack.size} pieces, distrib is $distrib")
        var height = -1
        val fallenTiles: Map<Pos, List<Piece>> = distrib.mapIndexed { steps, nbTiles -> // move step by step
            val newPos = pos.move(dir, steps + 1)
            newPos to (0 until nbTiles).map { // position each tile in that step
                height++
                val piece = stack[height]
                val tower = board.towerAt(newPos) ?: return MoveOutcome.illegal(board, this, "Falling outside the board")
                when (tower.topPiece) { // check receiver must be able to accept the piece
                    null -> piece
                    is CapStone -> return MoveOutcome.illegal(board, this, "Pieces falling onto a CapStone at $newPos")
                    is ReserveTile -> throw UnsupportedOperationException("There should never be a ReserveTile in a Stack")
                    is Road ->  piece
                    is Wall -> {
                        if (piece.piece == CAPSTONE) { // crushing the wall
                            crushedWall = newPos to tower.pieces.size - 1
                            piece
                        } else {
                            return MoveOutcome.illegal(board, this, "Pieces falling onto a Wall at $newPos")
                        }
                    }
                }
            }
        }.toMap()
        val originalTower = board.towerAt(pos)!!
        val depletedStack = mapOf(pos to originalTower.pieces.indexOf(stack.first()))
        val removedTiles = if (crushedWall == null) {
            depletedStack
        } else {
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

    override fun applyTo(board: Board): MoveOutcome {
        val tower = board.towerAt(pos) ?: return MoveOutcome.illegal(board, this, "Outside board range: $pos, board size is ${board.size}")
        if (tower.pieces.isNotEmpty()) return MoveOutcome.illegal(board, this, "Board has a non-empty tower at $pos, cannot put a Wall there from the reserve")
        val newBoard = board.change(
            emptyMap(),
            mapOf(pos to listOf(Wall(player))),
            consumedTile = true
        )
        return MoveOutcome(board, this, newBoard)
    }
}

data class PlaceReserveRoad(val player: Boolean, val pos: Pos) : Move {

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
