package org.marmotte.tak.display.parts

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.gameplay.UIState
import java.awt.Color
import java.awt.Graphics2D

class AvailableMoves(private val uiState: UIState) : Drawable {

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        val selected = uiState.pieceSelected ?: return
        val legalMoves = uiState.board.generateLegalMovesFrom(selected)
        legalMoves.forEach { it.draw(g, updateContext) }
    }
}