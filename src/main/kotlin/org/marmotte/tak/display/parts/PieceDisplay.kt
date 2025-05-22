package org.marmotte.tak.display.parts

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.engine.Pos
import org.marmotte.tak.gameplay.UIState
import java.awt.Color
import java.awt.Graphics2D

class PieceDisplay(private val uiState: UIState) : Drawable {

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        val board = uiState.board
        g.color = Color.BLACK
        for (x in 0..5) {
            for (y in 0..5) {
                val tileNum = Pos(x, y)
                board.pieceAt(tileNum).draw(g, updateContext)
            }
        }
    }
}