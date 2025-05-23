package org.marmotte.tak.display.parts

import org.marmotte.tak.display.drawOval
import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.engine.Pos
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D


class BoardBackGround(private val sizeGetter: () -> Int) : Drawable {

    companion object {
        private val lineStroke = BasicStroke(3.0f)
        private val RADIUS = 0.15
    }

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        val size = sizeGetter()
        // Tiles in alternating colors
        g.color = ColorScheme.background
        g.fillRect(0, 0, (size + 1) * updateContext.scale, (size + 1) * updateContext.scale)
        // Draw lines
        g.color = ColorScheme.lines
        g.stroke = lineStroke
        for (i in 0..size) {
            g.drawLine(i * updateContext.scale, 0, i * updateContext.scale, updateContext.scale * (size + 1))
            g.drawLine(0, i * updateContext.scale, updateContext.scale * (size + 1), i * updateContext.scale)
        }
        for (i in 1..size) {
            for (j in 1..size) {
                g.fillOval(
                    (updateContext.scale * (i - RADIUS)).toInt(),
                    (updateContext.scale * (j - RADIUS)).toInt(),
                    (updateContext.scale * RADIUS * 2).toInt(),
                    (updateContext.scale * RADIUS * 2).toInt()
                )
            }
        }
        // Draw file letters and rank numbers
        g.color = ColorScheme.text
        for (i in 0 until size) {
            val letter = Pos(i, 0).fileLetter().toString()
            val letterRect: Rectangle2D = g.fontMetrics.getStringBounds(letter, g)
            g.drawString(
                letter,
                (updateContext.scale * (i + 1.0) - letterRect.width / 2).toInt(),
                (updateContext.scale * (size + 2.0).toInt())
            )
            val digit = (i + 1).toString()
            val digitRect: Rectangle2D = g.fontMetrics.getStringBounds(digit, g)
            g.drawString(
                (i + 1).toString(),
                (updateContext.scale * (size + 1.5) - digitRect.width / 2).toInt(),
                (updateContext.scale * (i + 2.0) - digitRect.height / 2).toInt()
            )
        }
    }
}
