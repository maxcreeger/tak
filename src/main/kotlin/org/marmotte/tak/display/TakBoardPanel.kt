package org.marmotte.tak.display

import org.marmotte.tak.controller.BoardController
import org.marmotte.tak.display.drawables.GraphicalInterface
import org.marmotte.tak.display.drawables.GraphicalInterfaceImpl
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.display.events.DeselectEvent
import org.marmotte.tak.display.events.HoveredTowerEvent
import org.marmotte.tak.display.events.SelectStackEvent
import org.marmotte.tak.display.parts.BoardBackGround
import org.marmotte.tak.display.parts.BoardMessage
import org.marmotte.tak.display.parts.PieceDisplay
import org.marmotte.tak.engine.*
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
    private val boardController: BoardController,
) : JPanel(), GraphicalInterface by GraphicalInterfaceImpl() {

    val pieceDisplay = PieceDisplay(uiState)

    init {
        isOpaque = true
        background = Color.black
        add(BoardBackGround {  uiState.board.size })
        add(BoardMessage(1, 10, true) { "${uiState.board.activePlayer.toPlayerName()} to play" })
        add(pieceDisplay)
        minimumSize = Dimension(MIN_SCALE * (uiState.board.size + 2), MIN_SCALE * (uiState.board.size + 2))
        preferredSize = Dimension(DEFAULT_SCALE * (uiState.board.size + 2), DEFAULT_SCALE * (uiState.board.size + 2))
        maximumSize = Dimension(MAX_SCALE * (uiState.board.size + 2), MAX_SCALE * (uiState.board.size + 2))

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
                                } else { // clicked on opponent's tower, not in control -> deselecting (we had nothing anyway)
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
                                    val preparedMove = uiState.preparedMove
                                    val dir = selected.tower.pos.dirTo(pos)
                                    if (selected.size == 1) { // simple case: single tile, no need to select a distribution
                                        boardController.onMove(StackMove(uiState.board.activePlayer, selected, selected.tower.pos, dir, listOf(1)))
                                    } else if (preparedMove == null) {
                                        val preparedMove = StackMove(
                                            uiState.board.activePlayer,
                                            selected,
                                            selected.tower.pos,
                                            dir,
                                            listOf(selected.size) // start the distribution with all tiles dropped on the very next tower
                                        )
                                        boardController.onPrepareMove(preparedMove)
                                    } else if (e.button == MouseEvent.BUTTON1) { // left-click: select the distribution
                                        val dist = pos.distTo(preparedMove.pos, preparedMove.dir) // dist from stack to click
                                        val distribThere = preparedMove.distrib.getOrNull(dist - 1)
                                        if (dist <= 0) {
                                            println("Failed distrib change (clicked in the wrong dir)")
                                            val newDirMove = StackMove(
                                                uiState.board.activePlayer,
                                                selected,
                                                selected.tower.pos,
                                                dir,
                                                listOf(selected.size)
                                            )
                                            boardController.onPrepareMove(newDirMove)
                                        } else if (distribThere == null) {
                                            println("Failed distrib change (no distrib where clicked)")
                                        } else if (distribThere <= 1) {
                                            println("Failed distrib change (not enough tiles left, min 1)")
                                        } else {
                                            val newDistrib = preparedMove.distrib.toMutableList()
                                            newDistrib[dist - 1]--
                                            if (newDistrib.size <= dist) {
                                                newDistrib.add(1)
                                            } else {
                                                newDistrib[dist]++
                                            }
                                            boardController.onPrepareMove(preparedMove.copy(distrib = newDistrib))
                                        }
                                    } else { // right-click: commit
                                        boardController.onMove(preparedMove)
                                    }
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


    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        paintDrawables(g, UpdateContext(scale(), uiState.hoveredStack, uiState.selectedStack))
    }

    private fun scale(): Int = min(
        size.height / (uiState.board.size + 4),
        size.width / (uiState.board.size + 2)
    ).coerceIn(MIN_SCALE, MAX_SCALE)

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
        pos: Pos,
    ): StackMove {
        val tower = stack.tower
        val dir = tower.pos.dirTo(pos)
        return StackMove(uiState.board.activePlayer, stack, tower.pos, dir, listOf(stack.size))
    }

    private fun generatePlaceCapStoneMove(capStone: CapStone, pos: Pos): PlaceCapStone {
        return PlaceCapStone(uiState.board.activePlayer, capStone, pos)
    }
}

