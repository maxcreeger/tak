package org.marmotte.tak.display

import org.marmotte.tak.controller.BoardController
import org.marmotte.tak.display.drawables.GraphicalInterface
import org.marmotte.tak.display.drawables.GraphicalInterfaceImpl
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.display.events.HoveredTowerEvent
import org.marmotte.tak.display.events.PlaceStackEvent
import org.marmotte.tak.display.events.SelectStackEvent
import org.marmotte.tak.display.parts.AvailableMoves
import org.marmotte.tak.display.parts.BoardBackGround
import org.marmotte.tak.display.parts.BoardMessage
import org.marmotte.tak.display.parts.PieceDisplay
import org.marmotte.tak.engine.Dir.*
import org.marmotte.tak.engine.Pos
import org.marmotte.tak.engine.StackOfPartialTower
import org.marmotte.tak.engine.StackOfReserveCapStone
import org.marmotte.tak.engine.StackOfReserveTile
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
import kotlin.math.abs
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
        paintDrawables(g, UpdateContext(scale(), uiState.hoveredPiece, uiState.selectedStack))
    }

    private fun scale(): Int = min(
        size.height / (uiState.board.size + 2),
        size.width / (uiState.board.size + 2)
    ).coerceIn(MIN_SCALE, MAX_SCALE)

    fun addBoardController(boardController: BoardController) {
        addMouseListener(
            object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent?) {
                    super.mousePressed(e)
                    val scale = scale()
                    if (e != null) {
                        val row = e.x / scale
                        val file = e.y / scale
                        when (val stack = uiState.selectedStack) {
                            null -> {
                                val tower = uiState.board.pieceAt(Pos(row, file)) ?: return
                                if(tower.pieces().isNotEmpty()) {
                                    val stack = StackOfPartialTower(
                                        tower, 0
                                        /** TODO Compute actual height here */
                                    )
                                    boardController.onSelect(SelectStackEvent(uiState.board.activePlayer, stack, e))
                                }
                            }

                            is StackOfPartialTower -> {
                                val tower = stack.tower
                                val northSouth = abs(file - tower.pos.x) < abs(row - tower.pos.y)
                                val dir = if (northSouth) {
                                    if (file > tower.pos.x) EAST else WEST
                                } else {
                                    if (row > tower.pos.y) SOUTH else NORTH
                                }
                                boardController.onPlaceStack(PlaceStackEvent(stack, file, row, dir, e))
                            }

                            is StackOfReserveCapStone -> TODO()
                            is StackOfReserveTile -> TODO()
                        }
                    }
                }
            }
        )
        addMouseMotionListener(
            object : MouseMotionAdapter() {
                override fun mouseMoved(e: MouseEvent?) {
                    super.mouseMoved(e)
                    val scale = scale()
                    if (e != null) {
                        val row = e.x / scale
                        val file = e.y / scale
                        val tower = uiState.board.pieceAt(Pos(file, row)) ?: return
                        if(tower.pieces().isEmpty()) {
                            // TODO show potential move??
                            return
                        }
                        boardController.onHover(
                            HoveredTowerEvent(
                                file, row, tower.pieces().last()
                                /** TODO not necessarily the last one*/
                                , e
                            )
                        )
                    }
                }
            }
        )
    }
}

