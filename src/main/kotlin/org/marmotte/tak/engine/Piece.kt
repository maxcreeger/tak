package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.display.parts.ColorScheme
import org.marmotte.tak.engine.Piece.Companion.toPolygon
import java.awt.Graphics2D
import java.awt.Polygon
import java.awt.Rectangle

sealed interface Piece {

    companion object{
        fun Rectangle.toPolygon(): Polygon {
            val xpoints = intArrayOf(x, x + width, x + width, x)
            val ypoints = intArrayOf(y, y, y + height, y + height)
            return Polygon(xpoints, ypoints, 4)
        }
    }

    val piece: PieceType
    val player: Boolean
    fun getPolygon(x: Double, y: Double, scale: Int): Polygon
    fun drawAt(x: Double, y: Double, g: Graphics2D, updateContext: UpdateContext) {
        val polygon = getPolygon(x, y, updateContext.scale)
        val fillColor = when {
            updateContext.selectedStack?.contains(this) ?: false -> ColorScheme.selected
            player -> ColorScheme.whitePlayer
            else -> ColorScheme.blackPlayer
        }
        val drawColor = when {
            updateContext.highlightedStack?.contains(this) ?: false -> ColorScheme.highlight
            player -> ColorScheme.blackPlayer
            else -> ColorScheme.whitePlayer
        }
        g.color = fillColor
        g.fillPolygon(polygon)
        g.color = drawColor
        g.drawPolygon(polygon)
    }
}

sealed class Tile(override val player: Boolean) : Piece {

    companion object {
        const val WIDTH = 0.5
    }

    override fun getPolygon(x: Double, y: Double, scale: Int): Polygon{
        return Rectangle(
            (scale * (x - WIDTH / 2)).toInt(),
            (scale * (y - WIDTH / 2)).toInt(),
            (scale * (WIDTH)).toInt(),
            (scale * (WIDTH)).toInt()
        ).toPolygon()
    }
}

class ReserveTile(player: Boolean) : Tile(player){
    override val piece: PieceType = PieceType.RESERVE
}

class Road(player: Boolean) : Tile(player) {
    override val piece: PieceType = PieceType.ROAD
}

class Wall(override val player: Boolean) : Piece {

    companion object {
        const val WIDTH = 0.5
        const val THICKNESS = 0.1
    }

    override val piece: PieceType = PieceType.WALL

    override fun getPolygon(x: Double, y: Double, scale: Int): Polygon {
        val xCoords = intArrayOf(
            (scale * (x + WIDTH / 2 - THICKNESS)).toInt(),
            (scale * (x - WIDTH / 2)).toInt(),
            (scale * (x - WIDTH / 2 + THICKNESS)).toInt(),
            (scale * (x + WIDTH / 2)).toInt(),
        )
        val yCoords = intArrayOf(
            (scale * (y + WIDTH / 2 + THICKNESS)).toInt(),
            (scale * (y - WIDTH / 2 + THICKNESS)).toInt(),
            (scale * (y - WIDTH / 2)).toInt(),
            (scale * (y + WIDTH / 2)).toInt(),
        )
        return Polygon(xCoords, yCoords, 4)
    }
}

class CapStone(override val player: Boolean) : Piece {

    companion object {
        const val HEIGHT = 0.5
        const val THICKNESS = 0.1
        const val WIDTH = 0.5
    }

    override val piece: PieceType = PieceType.CAPSTONE

    override fun getPolygon(x: Double, y: Double, scale: Int): Polygon {
        val xCoords = intArrayOf(
            (scale * (x - WIDTH / 2)).toInt(),
            (scale * (x - THICKNESS / 2)).toInt(),
            (scale * (x - WIDTH / 2)).toInt(),
            (scale * (x + WIDTH / 2)).toInt(),
            (scale * (x + THICKNESS / 2)).toInt(),
            (scale * (x + WIDTH / 2)).toInt(),
        )
        val yCoords = intArrayOf(
            (scale * (y + HEIGHT / 2)).toInt(),
            (scale * y).toInt(),
            (scale * (y - HEIGHT / 2)).toInt(),
            (scale * (y - HEIGHT / 2)).toInt(),
            (scale * y).toInt(),
            (scale * (y + HEIGHT / 2)).toInt(),
        )
        return Polygon(xCoords, yCoords, 6)
    }
}
