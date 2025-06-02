package org.marmotte.tak.engine

sealed interface Piece {

    val player: Boolean

    fun <I, O> accept(visitor: PieceVisitor<I, O>, input: I): O
}

sealed class Tile(override val player: Boolean) : Piece {

    companion object {
        const val WIDTH = 0.5
    }

    override fun toString(): String = "${javaClass.simpleName}(${player.toPlayerName()})"
}

fun Boolean.toPlayerName(): String = if(this) "White" else "Black"

class ReserveTile(player: Boolean) : Tile(player) {
    override fun <I, O> accept(visitor: PieceVisitor<I, O>, input: I): O {
        return visitor.visit(this, input)
    }
}

class Road(player: Boolean) : Tile(player){
    override fun <I, O> accept(visitor: PieceVisitor<I, O>, input: I): O {
        return visitor.visit(this, input)
    }
}

class Wall(override val player: Boolean) : Piece {

    companion object {
        const val WIDTH = 0.5
        const val THICKNESS = 0.1
    }

    override fun <I, O> accept(visitor: PieceVisitor<I, O>, input: I): O {
        return visitor.visit(this, input)
    }

    override fun toString(): String = "Wall(${player.toPlayerName()})"
}

class CapStone(override val player: Boolean) : Piece {

    companion object {
        const val HEIGHT = 0.5
        const val THICKNESS = 0.1
        const val WIDTH = 0.5
    }

    override fun <I, O> accept(visitor: PieceVisitor<I, O>, input: I): O {
        return visitor.visit(this, input)
    }

    override fun toString(): String = "CapStone(${player.toPlayerName()})"
}

interface PieceVisitor<I,O> {
    fun visit(road: Road, input: I): O
    fun visit(wall: Wall, input: I): O
    fun visit(capStone: CapStone, input: I): O
    fun visit(reserveTile: ReserveTile, input: I): O
}
