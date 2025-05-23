package org.marmotte.tak.display.parts

import org.marmotte.tak.display.PromotionListener
import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.GraphicalInterface
import org.marmotte.tak.display.drawables.GraphicalInterfaceImpl
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.engine.CapStone
import org.marmotte.tak.engine.Road
import org.marmotte.tak.gameplay.Display.Companion.DEFAULT_SCALE
import org.marmotte.tak.gameplay.Display.Companion.MAX_SCALE
import org.marmotte.tak.gameplay.Display.Companion.MIN_SCALE
import org.marmotte.tak.gameplay.UIState
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import kotlin.math.min

class RemainingPiecesPanel(
    private val uiState: UIState,
    val color: Boolean,
) : GraphicalInterface by GraphicalInterfaceImpl(), Drawable, JPanel() {

    companion object {
        private const val NB_ROWS = 5
        private const val NB_COLS = 1
    }

    init {
        isOpaque = true
        background = Color.black
        minimumSize = Dimension(MIN_SCALE * NB_COLS, MIN_SCALE * NB_ROWS)
        preferredSize = Dimension(DEFAULT_SCALE * NB_COLS, DEFAULT_SCALE * NB_ROWS)
        maximumSize = Dimension(MAX_SCALE * NB_COLS, MAX_SCALE * NB_ROWS)
        add(BoardMessage(1, 10, true) { "${uiState.board.activePlayer} to play" })
        add(this as Drawable)
        background = ColorScheme.background
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        paintDrawables(g, UpdateContext(scale(), null))
    }

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        val reserve = uiState.board.reserveOf(color)
        reserve
            .filterIsInstance<Road>()
            .forEachIndexed { index, piece ->
                piece.drawAt((index % 5) * 0.05 + 0.45, 3 - (index / 5) * 0.4 - index * 0.2 + 0.5, g, updateContext)
            }
        reserve.find { it is CapStone }?.drawAt(0.5, 4.5, g, updateContext)
    }

    fun addPlacementListener(listener: PromotionListener) {
        addMouseListener(
            object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent?) {
                    super.mousePressed(e)
                    val scale = scale()
                    if (e != null) {
                        val row = e.x / scale
                        val file = e.y / scale
                        listener.onClick(PromotionEvent(row, file, e))
                    }
                }
            }
        )
    }

    private fun scale(): Int = min(
        size.height / NB_ROWS,
        size.width / NB_COLS
    ).coerceIn(MIN_SCALE, MAX_SCALE)
}