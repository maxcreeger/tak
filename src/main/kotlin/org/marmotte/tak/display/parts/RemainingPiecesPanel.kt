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
        private const val NB_ROWS = 5
        private const val NB_COLS = 1
        private const val FULL_HEIGHT = 4.0
        private const val TILE_STAGGER_HEIGHT = 0.1
        private const val STACK_STAGGER_HEIGHT = 1.0
        private const val TILE_STAGGER_WIDTH = 0.05
        private const val TILES_PER_STACK = 5
        private const val CAPSTONE_POS = 4.0

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
        return Point2D.Double(
            (index % TILES_PER_STACK) * TILE_STAGGER_WIDTH + 0.45,
            3 - (index / TILES_PER_STACK) * STACK_STAGGER_HEIGHT - (index % TILES_PER_STACK) * TILE_STAGGER_HEIGHT + 0.5,
        )
    }

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        g.color = ColorScheme.background.darker()
        g.fillRect(0, (updateContext.scale * FULL_HEIGHT).toInt(), updateContext.scale, updateContext.scale)
        val reserve = uiState.board.reserveOf(player)
        reserve.tiles.forEachIndexed { index, piece ->
            val pixelPos = getTilePixelPos(index)
            piece.drawAt(pixelPos.x, pixelPos.y, g, updateContext)
        }
        reserve.capstone?.drawAt(0.5, CAPSTONE_POS + .5, g, updateContext)
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
                            boardController.onDeselect(DeselectEvent(uiState.board.activePlayer, e))
                        } else {
                            boardController.onSelect(SelectReserveTileEvent(hoveredTile, e))
                        }
                    } else {
                        val hoveredCapStone = getHoveredCapstone(e.point, scale())
                        if (hoveredCapStone == null || selected is StackOfReserveCapStone && selected.capStone == hoveredCapStone) {
                            boardController.onDeselect(DeselectEvent(player, e))
                        } else {
                            boardController.onSelect(SelectReserveCapStoneEvent(player, hoveredCapStone, e))
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
                        boardController.onHover(HoveredReserveEvent(StackOfReserveTile(hoveredTile), e))
                    } else {
                        val hoveredCapStone = getHoveredCapstone(e.point, scale())
                        if (hoveredCapStone != null) {
                            boardController.onHover(HoveredReserveEvent(StackOfReserveCapStone(hoveredCapStone), e))
                        }
                    }
                }
            }
        })
    }


    fun getHoveredCapstone(e: Point, scale: Int): CapStone? {
        val capStone = uiState.board.reserveOf(player).capstone
        if (capStone != null) {
            val shape = capStone.getPolygon(0.5, +CAPSTONE_POS + 0.5, scale)
            if (shape.contains(e)) {
                return capStone
            }
        }
        return null
    }

    fun getHoveredTile(e: Point, scale: Int): ReserveTile? {
        var hoveredPiece: ReserveTile? = null
        val reserve = uiState.board.reserveOf(player)
        reserve.tiles.forEachIndexed { index, tile ->
            val pixelPos = getTilePixelPos(index)
            val rect = tile.getPolygon(pixelPos.x, pixelPos.y, scale)
            if (rect.contains(e)) {
                hoveredPiece = tile
            }
        }
        return hoveredPiece
    }

    private fun scale(): Int = min(
        size.height / NB_ROWS, size.width / NB_COLS
    ).coerceIn(MIN_SCALE, MAX_SCALE)
}