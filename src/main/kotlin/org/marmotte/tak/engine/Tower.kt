package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import java.awt.Graphics2D
import java.awt.geom.Point2D

class Tower(val pos: Pos) : Drawable {

    private val tower = mutableListOf<Piece>()

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        tower.forEachIndexed { height, piece ->
            val point = getPieceCenter(height)
            piece.drawAt(point.x, point.y, g, updateContext)
        }
    }

    fun getPieceCenter(height: Int): Point2D.Double {
        return Point2D.Double(pos.file + 1.0 + height / 15.0, pos.row + 1.0 - height / 8.0)
    }

    fun pieces(): List<Piece> = tower
    fun add(piece: Piece) {
        tower.add(piece)
    }

}
