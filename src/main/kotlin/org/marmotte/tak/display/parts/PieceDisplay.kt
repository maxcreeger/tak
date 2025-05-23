package org.marmotte.tak.display.parts

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.engine.Pos
import org.marmotte.tak.gameplay.UIState
import java.awt.Color
import java.awt.Graphics2D

class PieceDisplay(private val uiState: UIState) : Drawable {

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        uiState.board.draw(g, updateContext)
    }
}