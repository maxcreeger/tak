package org.marmotte.tak.display.parts

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.engine.Pos
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D

object BoardBackGround : Drawable {

    private val white = Color(238, 238, 210)
    private val black = Color(42, 42, 42)
    private val background = Color(118, 150, 86)
    private val lines = Color(82, 82, 82)
    val lineStroke = BasicStroke(3.0f)

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        // Tiles in alternating colors
        g.color = background
        g.fillRect(0, 0, 5*updateContext.scale, 5*updateContext.scale)
        // Draw lines
        g.color = lines
        g.stroke = lineStroke
        for (i in 0..8) {
            g.drawLine(i * updateContext.scale, 0, i * updateContext.scale, updateContext.scale * 5)
            g.drawLine(0, i * updateContext.scale, updateContext.scale * 5, i * updateContext.scale)
        }
        // Draw file letters and rank numbers
        g.color = Color.WHITE
        for (i in 0..7) {
            g.drawString(Pos(i, 0).fileLetter().toString(), i * updateContext.scale, 6 * updateContext.scale)
            g.drawString((i + 1).toString(), 5 * updateContext.scale + updateContext.scale / 5, (i + 1) * updateContext.scale)
        }
    }
}