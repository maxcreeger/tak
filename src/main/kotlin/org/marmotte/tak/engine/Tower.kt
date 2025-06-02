package org.marmotte.tak.engine

class Tower(
    val pos: Pos,
    private val pieces: List<Piece> = emptyList(),
): List<Piece> by pieces{

    val topPiece: Piece? by lazy {
        pieces.lastOrNull()
    }

    val owner: Boolean? by lazy {
        topPiece?.player
    }

    fun add(towerChanges: List<Piece>): Tower {
        return Tower(pos, pieces + towerChanges)
    }

    fun cutFrom(cutHeight: Int): Tower {
        return Tower(pos, pieces.subList(0, cutHeight))
    }

    override fun toString(): String {
        return "Tower(pos=$pos,pieces=$pieces)"
    }

    fun stackFrom(height: Int): StackOfPartialTower {
        return StackOfPartialTower(this, get(height))
    }

    fun stackFrom(piece: Piece): StackOfPartialTower {
        return StackOfPartialTower(this, piece)
    }
}
