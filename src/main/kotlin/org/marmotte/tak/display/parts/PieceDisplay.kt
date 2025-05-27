package org.marmotte.tak.display.parts

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.engine.Pos
import org.marmotte.tak.engine.Tower
import org.marmotte.tak.gameplay.UIState
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.event.MouseEvent
import java.awt.geom.Point2D

class PieceDisplay(private val uiState: UIState) : Drawable {

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        val size = uiState.board.size
        g.stroke = BasicStroke(1f)
        for (x in 0 until size) {
            for (y in 0 until size) {
                val tileNum = Pos(x, y)
                uiState.board.towerAt(tileNum)?.draw(g, updateContext)
            }
        }
    }


    private fun Tower.draw(g: Graphics2D, updateContext: UpdateContext) {
        pieces.forEachIndexed { height, piece ->
            val point = getPieceCenter(this, height)
            piece.drawAt(point.x, point.y, g, updateContext)
        }
    }

    fun getPieceCenter(tower: Tower, height: Int): Point2D.Double {
        return Point2D.Double(tower.pos.file + 1.0 + height / 15.0, tower.pos.row + 1.0 - height / 8.0)
    }

    fun getPos(e: MouseEvent, scale: Int): Pos {
        return Pos((e.x.toDouble()/ scale - 0.5).toInt(), (e.y.toDouble() / scale - 0.5).toInt())
    }
}
