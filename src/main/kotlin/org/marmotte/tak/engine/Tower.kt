package org.marmotte.tak.engine

import kotlin.math.max

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

    fun change(towerChanges: Map<Int, Piece?>): Tower {
        val newPieces = mutableListOf<Piece>()
        var stop = false
        for (height in 0..max(pieces.size, towerChanges.keys.max())) {
            val newPiece = towerChanges.getOrElse(height) { pieces.getOrNull(height) }
            if (newPiece != null) {
                if(stop) {
                    throw UnsupportedOperationException("Making a tower with holes in it. Current tower: $this changes: $towerChanges")
                }
                newPieces.add(newPiece)
            } else {
                stop = true
            }
        }
        return Tower(pos, newPieces)
    }
}
