package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.display.parts.ColorScheme
import java.awt.Graphics2D

interface Piece {
    fun drawAt(x: Double, y: Double, g: Graphics2D, updateContext: UpdateContext)
    val piece: PieceType
    val player: Boolean
}

abstract class Tile(override val player: Boolean) : Piece {

    companion object {
        const val WIDTH = 0.5
    }

    override val piece: PieceType = PieceType.ROAD

    override fun drawAt(x: Double, y: Double, g: Graphics2D, updateContext: UpdateContext) {
        g.color = if (player) ColorScheme.whitePlayer else ColorScheme.blackPlayer
        g.fillRect(
            (updateContext.scale * (x - WIDTH / 2)).toInt(),
            (updateContext.scale * (y - WIDTH / 2)).toInt(),
            (updateContext.scale * (WIDTH)).toInt(),
            (updateContext.scale * (WIDTH)).toInt()
        )
        g.color = when {
            this == updateContext.highlightedPiece -> ColorScheme.highlight
            player -> ColorScheme.blackPlayer
            else -> ColorScheme.whitePlayer
        }
        g.drawRect(
            (updateContext.scale * (x - WIDTH / 2)).toInt(),
            (updateContext.scale * (y - WIDTH / 2)).toInt(),
            (updateContext.scale * (WIDTH)).toInt(),
            (updateContext.scale * (WIDTH)).toInt()
        )
    }
}

class ReserveTile(player: Boolean): Tile(player)

class Road(player: Boolean): Tile(player)

class Wall(override val player: Boolean) : Piece {

    companion object {
        const val WIDTH = 0.5
        const val THICKNESS = 0.1
    }

    override val piece: PieceType = PieceType.WALL
    override fun drawAt(x: Double, y: Double, g: Graphics2D, updateContext: UpdateContext) {
        val xcoords = intArrayOf(
            (updateContext.scale * (x + WIDTH / 2 - THICKNESS)).toInt(),
            (updateContext.scale * (x - WIDTH / 2)).toInt(),
            (updateContext.scale * (x - WIDTH / 2 + THICKNESS)).toInt(),
            (updateContext.scale * (x + WIDTH / 2)).toInt(),
        )
        val yCoords = intArrayOf(
            (updateContext.scale * (y + WIDTH / 2 + THICKNESS)).toInt(),
            (updateContext.scale * (y - WIDTH / 2 + THICKNESS)).toInt(),
            (updateContext.scale * (y - WIDTH / 2)).toInt(),
            (updateContext.scale * (y + WIDTH / 2)).toInt(),
        )
        g.color = if (player) ColorScheme.whitePlayer else ColorScheme.blackPlayer
        g.fillPolygon(xcoords, yCoords, 4)
        g.color = if (player) ColorScheme.blackPlayer else ColorScheme.whitePlayer
        g.drawPolygon(xcoords, yCoords, 4)
    }
}

class CapStone(override val player: Boolean) : Piece {

    companion object {
        const val HEIGHT = 0.5
        const val THICKNESS = 0.1
        const val WIDTH = 0.5
    }

    override val piece: PieceType = PieceType.CAPSTONE

    override fun drawAt(x: Double, y: Double, g: Graphics2D, updateContext: UpdateContext) {
        g.color = if (player) ColorScheme.whitePlayer else ColorScheme.blackPlayer
        val xcoords = intArrayOf(
            (updateContext.scale * (x - WIDTH / 2)).toInt(),
            (updateContext.scale * (x - THICKNESS / 2)).toInt(),
            (updateContext.scale * (x - WIDTH / 2)).toInt(),
            (updateContext.scale * (x + WIDTH / 2)).toInt(),
            (updateContext.scale * (x + THICKNESS / 2)).toInt(),
            (updateContext.scale * (x + WIDTH / 2)).toInt(),
        )
        val yCoords = intArrayOf(
            (updateContext.scale * (y + HEIGHT / 2)).toInt(),
            (updateContext.scale * y).toInt(),
            (updateContext.scale * (y - HEIGHT / 2)).toInt(),
            (updateContext.scale * (y - HEIGHT / 2)).toInt(),
            (updateContext.scale * y).toInt(),
            (updateContext.scale * (y + HEIGHT / 2)).toInt(),
        )
        g.color = if (player) ColorScheme.whitePlayer else ColorScheme.blackPlayer
        g.fillPolygon(xcoords, yCoords, 6)
        g.color = if (player) ColorScheme.blackPlayer else ColorScheme.whitePlayer
        g.drawPolygon(xcoords, yCoords, 6)
    }
}
