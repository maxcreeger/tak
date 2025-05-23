package org.marmotte.tak.display.drawables

import org.marmotte.tak.engine.Piece

data class UpdateContext(
    var scale: Int = 100,
    var highlightedPiece: Piece?
)