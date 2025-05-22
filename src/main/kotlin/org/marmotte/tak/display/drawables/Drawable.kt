package org.marmotte.tak.display.drawables

import java.awt.Graphics2D

interface Drawable {

    fun draw(g: Graphics2D, updateContext: UpdateContext)

    operator fun plus(other: Drawable): Drawable {
        return DrawableGroup(listOf(this, other))
    }

}