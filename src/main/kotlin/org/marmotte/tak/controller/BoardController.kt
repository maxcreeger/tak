package org.marmotte.tak.controller

import org.marmotte.tak.display.events.*
import org.marmotte.tak.engine.Move
import org.marmotte.tak.engine.StackMove
import org.marmotte.tak.engine.StackOfReserveCapStone
import org.marmotte.tak.engine.StackOfReserveTile
import org.marmotte.tak.gameplay.UIState
import javax.swing.JFrame

class BoardController(
    private val uiState: UIState,
    private val frame: JFrame,
) {

    @Synchronized
    fun onHover(hoverEvent: HoveredTowerEvent) {
        uiState.setHover(hoverEvent.hoveredStack)
        frame.repaint()
    }

    @Synchronized
    fun onHover(hoverEvent: HoveredReserveEvent) {
        uiState.setHover(hoverEvent.hoveredStack)
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectReserveTileEvent) {
        if (selectEvent.reserveTile.player != uiState.board.activePlayer) return
        println("Selecting $selectEvent")
        uiState.setSelectedStack(StackOfReserveTile(selectEvent.reserveTile))
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectReserveCapStoneEvent) {
        if (selectEvent.player != uiState.board.activePlayer) return
        println("Selecting $selectEvent")
        uiState.setSelectedStack(StackOfReserveCapStone(selectEvent.capStone))
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectStackEvent) {
        if (selectEvent.player != uiState.board.activePlayer) return
        println("Selecting $selectEvent")
        uiState.setSelectedStack(selectEvent.stack)
        uiState.preparedMove = null
        frame.repaint()
    }

    @Synchronized
    fun onDeselect(deselectEvent: DeselectEvent) {
        uiState.setSelectedStack(null)
        uiState.preparedMove = null
        println("DeSelecting $deselectEvent")
        frame.repaint()
    }

    @Synchronized
    fun onPrepareMove(move: StackMove) {
        println("Preparing $move")
        val result = uiState.board.execute(move)
        if (!result.isLegal) {
            println("Yeah actually $move was not legal because ${result.message}")
        } else {
            uiState.preparedMove = move
            frame.repaint()
        }
    }

    @Synchronized
    fun onMove(move: Move) {
        println("Executing $move")
        val result = uiState.board.execute(move)
        if (!result.isLegal) {
            println("Yeah actually $move was not legal because ${result.message}")
        } else {
            uiState.preparedMove = null
            uiState.apply(result)
            frame.repaint()
        }
    }
}
