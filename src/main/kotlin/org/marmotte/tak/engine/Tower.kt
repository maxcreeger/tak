package org.marmotte.tak.engine

data class Tower(
    val pos: Pos,
    val pieces: List<Piece> = emptyList(),
) {

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
}
