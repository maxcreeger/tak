package org.marmotte.tak.display.drawables

import java.awt.Graphics2D

object NoDraw : Drawable {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        // Do nothing
    }
}