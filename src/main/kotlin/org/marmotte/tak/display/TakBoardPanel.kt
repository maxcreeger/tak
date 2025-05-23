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
    private val uiState: UIState,
) : JPanel(), GraphicalInterface by GraphicalInterfaceImpl() {

    init {
        isOpaque = true
        background = Color.black
        add(BoardBackGround(uiState.board::size))
        add(AvailableMoves(uiState)) // black
        add(BoardMessage(1, 10, true) { "${uiState.board.activePlayer} to play" })
        add(PieceDisplay(uiState))
        minimumSize = Dimension(MIN_SCALE * (uiState.board.size + 2), MIN_SCALE * (uiState.board.size + 2))
        preferredSize = Dimension(DEFAULT_SCALE * (uiState.board.size + 2), DEFAULT_SCALE * (uiState.board.size + 2))
        maximumSize = Dimension(MAX_SCALE * (uiState.board.size + 2), MAX_SCALE * (uiState.board.size + 2))
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        paintDrawables(g, UpdateContext(scale(), null))
    }

    private fun scale(): Int = min(
        size.height / (uiState.board.size + 2),
        size.width / (uiState.board.size + 2)
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

