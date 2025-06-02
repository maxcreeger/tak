package org.marmotte.tak.display.parts

import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.display.parts.PieceDisplay.toPolygon
import org.marmotte.tak.engine.*
import org.marmotte.tak.engine.CapStone.Companion.HEIGHT
import org.marmotte.tak.engine.CapStone.Companion.THICKNESS
import org.marmotte.tak.engine.Tile.Companion.WIDTH
import java.awt.Graphics2D
import java.awt.Polygon
import java.awt.Rectangle
import java.awt.geom.Point2D

object PieceDisplay {

    fun Rectangle.toPolygon(): Polygon {
        val xCoords = intArrayOf(x, x + width, x + width, x)
        val yCoords = intArrayOf(y, y, y + height, y + height)
        return Polygon(xCoords, yCoords, 4)
    }

    fun drawAt(piece: Piece, x: Double, y: Double, g: Graphics2D, updateContext: UpdateContext, phantom: Boolean = false) {
        val polygon = piece.accept(PieceOutline, PosAndScale(x, y, updateContext.scale))
        val fillColor = when {
            !phantom && updateContext.selectedStack?.contains(piece)?: false -> ColorScheme.selected
            piece.player -> ColorScheme.whitePlayer
            else -> ColorScheme.blackPlayer
        }
        val drawColor = when {
            phantom -> ColorScheme.phantom
            updateContext.highlightedStack?.contains(piece) ?: false -> ColorScheme.highlight
            piece.player -> ColorScheme.blackPlayer
            else -> ColorScheme.whitePlayer
        }
        g.color = fillColor
        g.fillPolygon(polygon)
        g.color = drawColor
        g.drawPolygon(polygon)
    }
}

data class PosAndScale(val x: Double, val y: Double, val scale: Int){
    constructor(pos: Point2D, scale: Int): this(pos.x, pos.y, scale)
}

object PieceOutline : PieceVisitor<PosAndScale, Polygon> {
    fun tilePolygon(input: PosAndScale): Polygon {
        with(input) {
            return Rectangle(
                (scale * (x - WIDTH / 2)).toInt(),
                (scale * (y - WIDTH / 2)).toInt(),
                (scale * (WIDTH)).toInt(),
                (scale * (WIDTH)).toInt()
            ).toPolygon()
        }
    }

    override fun visit(road: Road, input: PosAndScale): Polygon {
        return tilePolygon(input)
    }

    override fun visit(reserveTile: ReserveTile, input: PosAndScale): Polygon {
        return tilePolygon(input)
    }

    override fun visit(wall: Wall, input: PosAndScale): Polygon {
        with(input) {
            val xCoords = intArrayOf(
                (scale * (x + Wall.Companion.WIDTH / 2 - Wall.Companion.THICKNESS)).toInt(),
                (scale * (x - Wall.Companion.WIDTH / 2)).toInt(),
                (scale * (x - Wall.Companion.WIDTH / 2 + Wall.Companion.THICKNESS)).toInt(),
                (scale * (x + Wall.Companion.WIDTH / 2)).toInt(),
            )
            val yCoords = intArrayOf(
                (scale * (y + Wall.Companion.WIDTH / 2 + Wall.Companion.THICKNESS)).toInt(),
                (scale * (y - Wall.Companion.WIDTH / 2 + Wall.Companion.THICKNESS)).toInt(),
                (scale * (y - Wall.Companion.WIDTH / 2)).toInt(),
                (scale * (y + Wall.Companion.WIDTH / 2)).toInt(),
            )
            return Polygon(xCoords, yCoords, 4)
        }
    }

    override fun visit(capStone: CapStone, input: PosAndScale): Polygon {
        with(input) {
            val xCoords = intArrayOf(
                (scale * (x - CapStone.Companion.WIDTH / 2)).toInt(),
                (scale * (x - THICKNESS / 2)).toInt(),
                (scale * (x - CapStone.Companion.WIDTH / 2)).toInt(),
                (scale * (x + CapStone.Companion.WIDTH / 2)).toInt(),
                (scale * (x + THICKNESS / 2)).toInt(),
                (scale * (x + CapStone.Companion.WIDTH / 2)).toInt(),
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

}