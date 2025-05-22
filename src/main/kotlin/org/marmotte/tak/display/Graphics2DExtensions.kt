package org.marmotte.tak.display

import java.awt.Graphics2D
import java.awt.Point

fun Graphics2D.drawLine(x0: Double, y0: Double, x1: Double, y1: Double) {
    drawLine(x0.toInt(), y0.toInt(), x1.toInt(), y1.toInt())
}

fun Graphics2D.drawOval(center: Point, radius: Int) {
    drawOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius)
}

fun Graphics2D.fillOval(center: Point, radius: Int) {
    fillOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius)
}