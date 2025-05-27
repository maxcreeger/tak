package org.marmotte.tak.engine

sealed interface Stack {
    fun contains(piece: Piece) : Boolean
}

data class StackOfReserveTile(
    val reserveTile: ReserveTile
): Stack {
    override fun contains(piece: Piece) = piece == reserveTile
}

data class StackOfReserveCapStone(
    val capStone: CapStone
): Stack{
    override fun contains(piece: Piece) = piece == capStone
}

data class StackOfPartialTower(
    val tower: Tower,
    val fromPiece: Piece
): Stack{
    val startHeight = tower.pieces.indexOf(fromPiece)
    override fun contains(piece: Piece) = tower.pieces.drop(startHeight).contains(piece)
}

