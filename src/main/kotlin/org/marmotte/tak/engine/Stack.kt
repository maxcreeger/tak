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
    val fromHeight: Int
): Stack{
    override fun contains(piece: Piece) = tower.pieces().contains(piece)
}

