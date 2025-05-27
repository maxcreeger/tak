package org.marmotte.tak.display

import org.marmotte.tak.controller.BoardController
import org.marmotte.tak.display.drawables.GraphicalInterface
import org.marmotte.tak.display.drawables.GraphicalInterfaceImpl
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.display.events.DeselectEvent
import org.marmotte.tak.display.events.HoveredTowerEvent
import org.marmotte.tak.display.events.SelectStackEvent
import org.marmotte.tak.display.parts.AvailableMoves
import org.marmotte.tak.display.parts.BoardBackGround
import org.marmotte.tak.display.parts.BoardMessage
import org.marmotte.tak.display.parts.PieceDisplay
import org.marmotte.tak.engine.*
import org.marmotte.tak.engine.Dir.*
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

    val pieceDisplay = PieceDisplay(uiState)

    init {
        isOpaque = true
        background = Color.black
        add(BoardBackGround(uiState.board::size))
        add(AvailableMoves(uiState)) // black
        add(BoardMessage(1, 10, true) { "${uiState.board.activePlayer.toPlayerName()} to play" })
        add(pieceDisplay)
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
                        val hoveredStack = findHoveredStack(scale, e)
                        val pos = hoveredStack?.tower?.pos ?: pieceDisplay.getPos(e, scale)
                        when (val selected = uiState.selectedStack) {
                            null -> // New selection...
                                if (hoveredStack == null) { // new selection of empty square -> do nothing
                                    boardController.onDeselect(DeselectEvent(uiState.board.activePlayer))
                                } else if (hoveredStack.tower.owner == uiState.board.activePlayer) { // new selection of Stack -> select it
                                    boardController.onSelect(SelectStackEvent(uiState.board.activePlayer, hoveredStack))
                                } else { // clicked on opponent's tower, not in control -> deselecting
                                    boardController.onDeselect(DeselectEvent(uiState.board.activePlayer))
                                }

                            is StackOfPartialTower -> { // clicked on an existing stack...
                                if (selected.tower == hoveredStack?.tower) { // clicked on the same tower...
                                    if (selected.fromPiece == hoveredStack.fromPiece) { // de-selecting the exact same stack
                                        boardController.onDeselect(DeselectEvent(uiState.board.activePlayer))
                                    } else { // Changing the selected stack's height
                                        assert(hoveredStack.tower.owner == uiState.board.activePlayer) // should already have the right owner
                                        boardController.onSelect(SelectStackEvent(uiState.board.activePlayer, hoveredStack))
                                    }
                                } else { // moving a stack to an alternate square
                                    boardController.onMove(generateStackMove(selected, pos))
                                }
                            }

                            is StackOfReserveCapStone -> {
                                boardController.onMove(generatePlaceCapStoneMove(selected.capStone, pos))
                            }
                            is StackOfReserveTile -> {
                                if (e.button == MouseEvent.BUTTON1) {
                                    boardController.onMove(PlaceReserveRoad(uiState.board.activePlayer, pos))
                                } else {
                                    boardController.onMove(PlaceReserveWall(uiState.board.activePlayer, pos))
                                }
                            }
                        }
                    }
                }
            })
        addMouseMotionListener(
            object : MouseMotionAdapter() {
                override fun mouseMoved(e: MouseEvent?) {
                    super.mouseMoved(e)
                    if (e == null) return
                    val scale = scale()
                    val hoveredStack = findHoveredStack(scale, e)
                    if (hoveredStack != null) { // hovered a piece
                        boardController.onHover(HoveredTowerEvent(hoveredStack.tower.pos, hoveredStack))
                    } else {
                        val pos = pieceDisplay.getPos(e, scale)
                        val tower = uiState.board.towerAt(pos) ?: return
                        if (tower.pieces.isEmpty()) {
                            // TODO show potential move if a stack is selected??
                            return
                        }
                    }
                }
            }
        )
    }

    private fun findHoveredStack(scale: Int, e: MouseEvent): StackOfPartialTower? {
        val hoveredStack = uiState.board.towers.firstNotNullOfOrNull { tower ->
            tower
                .pieces
                .filterIndexed { height, piece ->
                    val pieceCenter = pieceDisplay.getPieceCenter(tower, height)
                    piece.getPolygon(pieceCenter.x, pieceCenter.y, scale).contains(e.point)
                }
                .lastOrNull()
                ?.let { StackOfPartialTower(tower, it) }
        }
        return hoveredStack
    }

    private fun generateStackMove(
        stack: StackOfPartialTower,
        pos: Pos
    ): StackMove {
        val tower = stack.tower
        val northSouth = abs(pos.file - tower.pos.file) < abs(pos.row - tower.pos.row)
        val dir = if (northSouth) {
            if (pos.file > tower.pos.file) EAST else WEST
        } else {
            if (pos.row > tower.pos.row) SOUTH else NORTH
        }
        return StackMove(uiState.board.activePlayer, stack, pos, dir)
    }

    private fun generatePlaceCapStoneMove(capStone: CapStone, pos: Pos): PlaceCapStone {
        return PlaceCapStone(uiState.board.activePlayer, capStone, pos)
    }
}

