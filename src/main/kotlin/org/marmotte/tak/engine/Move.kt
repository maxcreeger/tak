package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.display.parts.ColorScheme
import org.marmotte.tak.engine.Tile.Companion.WIDTH
import java.awt.Graphics2D

interface Move : Drawable {
}

data class PlaceNewRoad(
    val player: Boolean,
    val to: Pos,
) : Move{
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        Road(player).drawAt(to.x.toDouble(), to.y.toDouble(), g, updateContext)
        g.color = if (player) ColorScheme.whitePlayer else ColorScheme.blackPlayer
        g.fillRect(
            (updateContext.scale * (to.x - WIDTH / 2)).toInt(),
            (updateContext.scale * (to.y - WIDTH / 2)).toInt(),
            (updateContext.scale * (WIDTH)).toInt(),
            (updateContext.scale * (WIDTH)).toInt()
        )
        g.color = ColorScheme.highlight
        g.drawRect(
            (updateContext.scale * (to.x - WIDTH / 2)).toInt(),
            (updateContext.scale * (to.y - WIDTH / 2)).toInt(),
            (updateContext.scale * (WIDTH)).toInt(),
            (updateContext.scale * (WIDTH)).toInt()
        )
    }
}

data class PlaceNewWall(
    val player: Boolean,
    val to: Pos,
) : Move{
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        g.color = if (player) ColorScheme.whitePlayer else ColorScheme.blackPlayer
        g.fillRect(
            (updateContext.scale * (to.x - WIDTH / 2)).toInt(),
            (updateContext.scale * (to.y - WIDTH / 2)).toInt(),
            (updateContext.scale * (WIDTH)).toInt(),
            (updateContext.scale * (WIDTH)).toInt()
        )
        g.color = ColorScheme.highlight
        g.drawRect(
            (updateContext.scale * (to.x - WIDTH / 2)).toInt(),
            (updateContext.scale * (to.y - WIDTH / 2)).toInt(),
            (updateContext.scale * (WIDTH)).toInt(),
            (updateContext.scale * (WIDTH)).toInt()
        )
    }
}