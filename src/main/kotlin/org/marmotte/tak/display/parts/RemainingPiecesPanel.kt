package org.marmotte.tak.display.parts

import org.marmotte.tak.controller.BoardController
import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.GraphicalInterface
import org.marmotte.tak.display.drawables.GraphicalInterfaceImpl
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.display.events.DeselectEvent
import org.marmotte.tak.display.events.HoveredReserveEvent
import org.marmotte.tak.display.events.SelectReserveCapStoneEvent
import org.marmotte.tak.display.events.SelectReserveTileEvent
import org.marmotte.tak.engine.CapStone
import org.marmotte.tak.engine.ReserveTile
import org.marmotte.tak.engine.StackOfReserveCapStone
import org.marmotte.tak.engine.StackOfReserveTile
import org.marmotte.tak.gameplay.Display.Companion.DEFAULT_SCALE
import org.marmotte.tak.gameplay.Display.Companion.MAX_SCALE
import org.marmotte.tak.gameplay.Display.Companion.MIN_SCALE
import org.marmotte.tak.gameplay.UIState
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.geom.Point2D
import javax.swing.JPanel
import kotlin.math.min

class RemainingPiecesPanel(
    private val uiState: UIState,
    val player: Boolean,
) : GraphicalInterface by GraphicalInterfaceImpl(), Drawable, JPanel() {

    companion object {
        private const val NB_ROWS = 6
        private const val NB_COLS = 2
        private const val TILE_STAGGER_HEIGHT = 0.1
        private const val TILE_STAGGER_WIDTH = 0.05
        private const val TILES_PER_STACK = 5
        private const val CAPSTONE_POS = NB_ROWS - 1
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
        paintDrawables(g, UpdateContext(scale(), uiState.hoveredStack, uiState.selectedStack))
    }

    fun getTilePixelPos(index: Int): Point2D.Double {
        val rowNum = index / (NB_COLS * TILES_PER_STACK)
        val colNum = (index / TILES_PER_STACK) % NB_COLS
        val height = index % TILES_PER_STACK
        return Point2D.Double(
            0.45 + colNum + height * TILE_STAGGER_WIDTH,
            CAPSTONE_POS - 0.5 - rowNum - height * TILE_STAGGER_HEIGHT,
        )
    }

    fun getCapStonePixelPos(index: Int): Point2D.Double {
        return Point2D.Double(
            index + 0.5,
            CAPSTONE_POS + 0.5,
        )
    }

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        g.color = ColorScheme.background.darker()
        g.fillRect(0, updateContext.scale * CAPSTONE_POS, updateContext.scale * NB_COLS, updateContext.scale * NB_ROWS)
        val reserve = uiState.board.reserveOf(player)
        reserve.tiles.forEachIndexed { index, tile ->
            val pixelPos = getTilePixelPos(index)
            tile.drawAt(pixelPos.x, pixelPos.y, g, updateContext)
        }
        reserve.capstones.forEachIndexed { index, capStone ->
            val pixelPos = getCapStonePixelPos(index)
            capStone.drawAt(pixelPos.x, pixelPos.y, g, updateContext)
        }
    }

    fun addBoardController(boardController: BoardController) {
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent?) {
                super.mousePressed(e)
                val selected = uiState.selectedStack
                if (e != null) {
                    val hoveredTile = getHoveredTile(e.point, scale())
                    if (hoveredTile != null) {
                        if (selected is StackOfReserveTile && hoveredTile == selected.reserveTile) {
                            boardController.onDeselect(DeselectEvent(uiState.board.activePlayer))
                        } else {
                            boardController.onSelect(SelectReserveTileEvent(hoveredTile))
                        }
                    } else {
                        val hoveredCapStone = getHoveredCapstone(e.point, scale())
                        if (hoveredCapStone == null || selected is StackOfReserveCapStone && selected.capStone == hoveredCapStone) {
                            boardController.onDeselect(DeselectEvent(player))
                        } else {
                            boardController.onSelect(SelectReserveCapStoneEvent(player, hoveredCapStone))
                        }
                    }
                }
            }
        })
        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(e: MouseEvent?) {
                super.mouseMoved(e)
                if (e != null) {
                    val hoveredTile = getHoveredTile(e.point, scale())
                    if (hoveredTile != null) {
                        boardController.onHover(HoveredReserveEvent(StackOfReserveTile(hoveredTile)))
                    } else {
                        val hoveredCapStone = getHoveredCapstone(e.point, scale())
                        if (hoveredCapStone != null) {
                            boardController.onHover(HoveredReserveEvent(StackOfReserveCapStone(hoveredCapStone)))
                        }
                    }
                }
            }
        })
    }


    fun getHoveredCapstone(e: Point, scale: Int): CapStone? {
        return uiState
            .board
            .reserveOf(player)
            .capstones
            .mapIndexedNotNull { index, capStone ->
                val pos = getCapStonePixelPos(index)
                val shape = capStone.getPolygon(pos.x, pos.y, scale)
                if (shape.contains(e)) {
                    index to capStone
                } else {
                    null
                }
            }
            .lastOrNull()
            ?.second
    }

    fun getHoveredTile(e: Point, scale: Int): ReserveTile? {
        return uiState
            .board
            .reserveOf(player)
            .tiles
            .mapIndexedNotNull { index, tile ->
                val pixelPos = getTilePixelPos(index)
                val rect = tile.getPolygon(pixelPos.x, pixelPos.y, scale)
                if (rect.contains(e)) {
                    index to tile
                } else {
                    null
                }
            }
            .lastOrNull()
            ?.second
    }

    private fun scale(): Int = min(
        size.height / NB_ROWS, size.width / NB_COLS
    ).coerceIn(MIN_SCALE, MAX_SCALE)
}