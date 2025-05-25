package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import java.awt.Graphics2D

class Tower(val pos: Pos) : Drawable {

    private val tower = mutableListOf<Piece>()

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        tower.forEachIndexed { height, piece ->
            piece.drawAt(pos.x + 1.0 + height / 15.0, pos.y + 1.0 - height / 8.0, g, updateContext)
        }
    }

    fun pieces(): List<Piece> = tower

}
