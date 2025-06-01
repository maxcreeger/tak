package org.marmotte.tak.display.drawables

import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D

interface GraphicalInterface {
    fun add(drawable: Drawable)
    fun addAll(drawables: List<Drawable>)
    fun remove(drawable: Drawable)
    fun paintDrawables(g: Graphics, updateContext: UpdateContext)
}

class GraphicalInterfaceImpl: GraphicalInterface {

    private val drawables = mutableListOf<Drawable>()

    override fun add(drawable: Drawable) {
        drawables.add(drawable)
    }

    override fun addAll(drawables: List<Drawable>) {
        this.drawables.addAll(drawables)
    }

    override fun remove(drawable: Drawable) {
        drawables.remove(drawable)
    }

    override fun paintDrawables(g: Graphics, updateContext: UpdateContext) {
        g.font = Font("TimesRoman", Font.PLAIN, updateContext.scale)
        drawables.forEach {
            it.draw(g as Graphics2D, updateContext)
        }
    }
}
