package org.marmotte.tak.engine

sealed interface Stack {
    fun contains(piece: Piece) : Boolean
    val pieces: List<Piece>
    val top: Piece
        get() = pieces.last()
    val bottom: Piece
        get() = pieces.first()
}

data class StackOfReserveTile(
    val reserveTile: ReserveTile
): Stack {
    override fun contains(piece: Piece) = piece == reserveTile
    override val pieces: List<ReserveTile> by lazy { listOf(reserveTile) }
}

data class StackOfReserveCapStone(
    val capStone: CapStone
): Stack{
    override fun contains(piece: Piece) = piece == capStone
    override val pieces: List<CapStone> by lazy { listOf(capStone)}
}

data class StackOfPartialTower(
    val tower: Tower,
    val fromPiece: Piece
): Stack{
    val startHeight = tower.pieces.indexOf(fromPiece)
    override fun contains(piece: Piece) = tower.pieces.drop(startHeight).contains(piece)
    override val pieces: List<Piece> by lazy { tower.pieces.subList(tower.pieces.indexOf(fromPiece), tower.pieces.size) }
}

