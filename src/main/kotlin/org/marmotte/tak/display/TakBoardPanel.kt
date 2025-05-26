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
        add(BoardMessage(1, 10, true) { "${if (uiState.board.activePlayer) "White" else "Black"} to play" })
        add(PieceDisplay(uiState))
        minimumSize = Dimension(MIN_SCALE * (uiState.board.size + 2), MIN_SCALE * (uiState.board.size + 2))
        preferredSize = Dimension(DEFAULT_SCALE * (uiState.board.size + 2), DEFAULT_SCALE * (uiState.board.size + 2))
        maximumSize = Dimension(MAX_SCALE * (uiState.board.size + 2), MAX_SCALE * (uiState.board.size + 2))
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        paintDrawables(g, UpdateContext(scale(), uiState.hoveredStack, uiState.selectedStack))
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
                        val file = e.x / scale
                        val row = e.y / scale
                        when (val stack = uiState.selectedStack) {
                            null -> {
                                val hoveredStack = uiState.board.towers.firstNotNullOfOrNull { tower ->
                                    tower
                                        .pieces()
                                        .filterIndexed { height, piece ->
                                            val pieceCenter = tower.getPieceCenter(height)
                                            piece.getPolygon(pieceCenter.x, pieceCenter.y, scale).contains(e.point)
                                        }
                                        .lastOrNull()
                                        ?.let { StackOfPartialTower(tower, it) }
                                }
                                if (hoveredStack != null) {
                                    boardController.onSelect(SelectStackEvent(uiState.board.activePlayer, hoveredStack, e))
                                }
                            }

                            is StackOfPartialTower -> {
                                val tower = stack.tower
                                val northSouth = abs(file - tower.pos.file) < abs(row - tower.pos.row)
                                val dir = if (northSouth) {
                                    if (file > tower.pos.file) EAST else WEST
                                } else {
                                    if (row > tower.pos.row) SOUTH else NORTH
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
                    if (e == null) return
                    val scale = scale()
                    val hoveredStack = uiState.board.towers.firstNotNullOfOrNull { tower ->
                        tower
                            .pieces()
                            .filterIndexed { height, piece ->
                                val pieceCenter = tower.getPieceCenter(height)
                                piece.getPolygon(pieceCenter.x, pieceCenter.y, scale).contains(e.point)
                            }
                            .lastOrNull()
                            ?.let { StackOfPartialTower(tower, it) }
                    }
                    if (hoveredStack != null) {
                        boardController.onHover(
                            HoveredTowerEvent(
                                hoveredStack.tower.pos.file,
                                hoveredStack.tower.pos.row,
                                hoveredStack,
                                e
                            )
                        )

                    } else {

                    }
                    val file = e.x / scale
                    val row = e.y / scale
                    val tower = uiState.board.towerAt(Pos(file, row)) ?: return
                    if (tower.pieces().isEmpty()) {
                        // TODO show potential move??
                        return
                    }
                }
            }
        )
    }
}

