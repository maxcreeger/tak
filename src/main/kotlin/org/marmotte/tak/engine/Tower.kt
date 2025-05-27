package org.marmotte.tak.engine

class Tower(val pos: Pos) {

    private val tower = mutableListOf<Piece>()

    fun pieces(): List<Piece> = tower
    fun add(piece: Piece) {
        tower.add(piece)
    }

    val owner: Boolean by lazy {
        pieces().last().player
    }

}
