package org.marmotte.tak.display.drawables

import java.awt.Graphics2D

class DrawableGroup(drawables: List<Drawable>) : Drawable {

    private val drawables = drawables.toMutableList()

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        drawables.forEach { it.draw(g, updateContext) }
    }
}