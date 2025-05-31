package org.marmotte.tak.engine

import org.marmotte.tak.engine.PieceType.CAPSTONE

interface Move {
    fun applyTo(board: Board): MoveOutcome
    fun <I, O> accept(visitor: MoveVisitor<I, O>, input: I): O
}

interface MoveVisitor<I, O> {
    fun visit(move: StackMove, input: I): O
    fun visit(move: PlaceReserveWall, input: I): O
    fun visit(move: PlaceReserveRoad, input: I): O
    fun visit(move: PlaceCapStone, input: I): O
}

data class StackMove(val player: Boolean, val stack: Stack, val pos: Pos, val dir: Dir, val distrib: List<Int>) : Move {

    override fun applyTo(board: Board): MoveOutcome {
        var crushedWall: Pair<Pos, Int>? = null
        if (distrib.sum() != stack.size) return MoveOutcome.illegal(board, this, "Bad distrib: stack has ${stack.size} pieces, distrib is $distrib")
        var height = -1
        val fallenTiles: Map<Pos, List<Piece>> = distrib.mapIndexed { steps, nbTiles -> // move step by step
            val newPos = pos.move(dir, steps + 1)
            newPos to (0 until nbTiles).flatMap { // position each tile in that step
                height++
                val piece = stack[height]
                val tower = board.towerAt(newPos) ?: return MoveOutcome.illegal(board, this, "Falling outside the board")
                when (val topPiece = tower.topPiece) { // check receiver must be able to accept the piece
                    null -> listOf(piece)
                    is CapStone -> return MoveOutcome.illegal(board, this, "Pieces falling onto a CapStone at $newPos")
                    is ReserveTile -> throw UnsupportedOperationException("There should never be a ReserveTile in a Stack")
                    is Road -> listOf(piece)
                    is Wall -> {
                        if (piece.piece == CAPSTONE) { // crushing the wall
                            crushedWall = newPos to tower.pieces.size - 1
                            listOf(Road(topPiece.player), piece) // crush the wall into a Road, then add the CapStone
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

    override fun <I, O> accept(visitor: MoveVisitor<I, O>, input: I): O {
        return visitor.visit(this, input)
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

    override fun <I, O> accept(visitor: MoveVisitor<I, O>, input: I): O {
        return visitor.visit(this, input)
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

    override fun <I, O> accept(visitor: MoveVisitor<I, O>, input: I): O {
        return visitor.visit(this, input)
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

    override fun <I, O> accept(visitor: MoveVisitor<I, O>, input: I): O {
        return visitor.visit(this, input)
    }
}
