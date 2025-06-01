package org.marmotte.tak.display.parts

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import java.awt.Color
import java.awt.Graphics2D

open class BoardMessage(
    private val file: Int,
    private val row: Int,
    private val active: Boolean = true,
    private val messageProvider: () -> String
) : Drawable {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        if(!active) return
        g.color = Color.WHITE
        g.drawString(
            messageProvider.invoke(),
            file * updateContext.scale,
            row * updateContext.scale
        )
    }
}