package org.marmotte.tak.engine

sealed interface Stack : List<Piece> {
    fun owner(): Boolean
}

sealed class StackImpl(
    protected val pieces: List<Piece>
) : Stack, List<Piece> by pieces {
    override fun owner()= last().player
}

data class StackOfReserveTile(
    val reserveTile: ReserveTile
) : StackImpl(listOf(reserveTile))

data class StackOfReserveCapStone(
    val capStone: CapStone
) : StackImpl(listOf(capStone))

data class StackOfPartialTower(
    val tower: Tower,
    val fromPiece: Piece,
) : StackImpl(tower.drop(tower.indexOf(fromPiece))) {
    fun move(dir: Dir, distrib: List<Int>): StackMove {
        return StackMove(owner(), this, tower.pos, dir, distrib)
    }
}

