package org.marmotte.tak.display

import org.marmotte.tak.display.drawables.GraphicalInterface
import org.marmotte.tak.display.drawables.GraphicalInterfaceImpl
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.display.parts.*
import org.marmotte.tak.gameplay.Display.Companion.DEFAULT_SCALE
import org.marmotte.tak.gameplay.Display.Companion.MAX_SCALE
import org.marmotte.tak.gameplay.Display.Companion.MIN_SCALE
import org.marmotte.tak.gameplay.UIState
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.JPanel
import kotlin.math.min

class TakBoardPanel(
    uiState: UIState,
) : JPanel(), GraphicalInterface by GraphicalInterfaceImpl() {

    companion object {
        private const val NB_ROWS = 10 // 8 cols + 1 row num + 1 row messages
        private const val NB_COLS = 9 // 8 files + letter display
    }

    init {
        isOpaque = true
        background = Color.black
        add(BoardBackGround)
        add(AvailableMoves(uiState))
        add(BoardMessage(1, 10, true) { "${uiState.board.activePlayer} to play" })
        add(PieceDisplay(uiState))
        minimumSize = Dimension(MIN_SCALE * NB_COLS, MIN_SCALE * NB_ROWS)
        preferredSize = Dimension(DEFAULT_SCALE * NB_COLS, DEFAULT_SCALE * NB_ROWS)
        maximumSize = Dimension(MAX_SCALE * NB_COLS, MAX_SCALE * NB_ROWS)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        paintDrawables(g, UpdateContext(scale()))
    }

    private fun scale(): Int = min(
        size.height / NB_ROWS,
        size.width / NB_COLS
    ).coerceIn(MIN_SCALE, MAX_SCALE)

    fun addTileClickListener(listener: TileClickListener) {
        addMouseListener(
            object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent?) {
                    super.mousePressed(e)
                    val scale = scale()
                    if (e != null) {
                        val row = e.x / scale
                        val file = e.y / scale
                        listener.onClick(TileEvent(row, file, e))
                    }
                }
            }
        )
    }

    fun addTileMotionListener(listener: TileMotionListener) {
        addMouseMotionListener(
            object : MouseMotionAdapter() {
                override fun mouseMoved(e: MouseEvent?) {
                    super.mouseMoved(e)
                    val scale = scale()
                    if (e != null) {
                        val row = e.x / scale
                        val file = e.y / scale
                        listener.onMove(TileEvent(row, file, e))
                    }
                }
            }
        )
    }
}

