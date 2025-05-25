package org.marmotte.tak.display.drawables

import org.marmotte.tak.engine.Piece
import org.marmotte.tak.engine.Stack

data class UpdateContext(
    var scale: Int = 100,
    var highlightedPiece: Piece?,
    var selectedStack: Stack?,
)