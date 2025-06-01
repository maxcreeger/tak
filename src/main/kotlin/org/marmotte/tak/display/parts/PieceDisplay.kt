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
        // Draw regular tiles
        for (x in 0 until size) {
            for (y in 0 until size) {
                val tilePos = Pos(Pos.file(x), y)
                uiState.board.towerAt(tilePos)?.draw(g, updateContext)
            }
        }
        g.stroke = BasicStroke(3f)
        // Draw 'phantom' tiles from the prepared Move
        val selected = uiState.selectedStack ?: return // no stack selected
        val preparedMove = uiState.preparedMove ?: return // no move prepared for that stack
        var total = 0
        preparedMove.distrib.forEachIndexed { moves, nbTiles ->
            val tilePos = preparedMove.pos.move(preparedMove.dir, 1 + moves)
            val tower = uiState.board.towerAt(tilePos) ?: return // illegal access
            for (height in tower.pieces.size until (tower.pieces.size + nbTiles)) {
                val point = getPieceCenter(tower, height)
                val piece = selected[total++]
                piece.drawAt(point.x, point.y, g, updateContext, phantom = true)
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
        return Point2D.Double(tower.pos.fileIndex() + 1.0 + height / 15.0, tower.pos.row + 1.0 - height / 8.0)
    }

    fun getPos(e: MouseEvent, scale: Int): Pos {
        val row = (e.y.toDouble() / scale - 0.5).toInt()
        val file = (e.x.toDouble() / scale - 0.5).toInt()
        return Pos(Pos.file(file), row)
    }
}
