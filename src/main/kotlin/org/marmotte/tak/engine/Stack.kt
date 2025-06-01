package org.marmotte.tak.engine

sealed interface Stack : List<Piece>

sealed class StackImpl(
    protected val pieces: List<Piece>
) : Stack, List<Piece> by pieces

data class StackOfReserveTile(
    val reserveTile: ReserveTile
) : StackImpl(listOf(reserveTile))

data class StackOfReserveCapStone(
    val capStone: CapStone
) : StackImpl(listOf(capStone))

data class StackOfPartialTower(
    val tower: Tower,
    val fromPiece: Piece,
) : StackImpl(tower.pieces.drop(tower.pieces.indexOf(fromPiece)))

