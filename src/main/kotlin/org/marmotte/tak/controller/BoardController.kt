package org.marmotte.tak.controller

import org.marmotte.tak.display.events.*
import org.marmotte.tak.engine.*
import org.marmotte.tak.gameplay.UIState
import javax.swing.JFrame

class BoardController(
    private val uiState: UIState,
    private val frame: JFrame,
) {

    @Synchronized
    fun onHover(hoverEvent: HoveredTowerEvent) {
        if (hoverEvent.row !in 0..5 || hoverEvent.file !in 0..5) {
            uiState.setHover(null)
        } else {
            uiState.setHover(hoverEvent.hoveredPiece)
        }
        frame.repaint()
    }

    @Synchronized
    fun onHover(hoverEvent: HoveredReserveEvent) {
        if (hoverEvent.hoveredPiece == null) {
            uiState.setHover(null)
        } else {
            uiState.setHover(hoverEvent.hoveredPiece)
        }
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectReserveTileEvent) {
        if(selectEvent.reserveTile.player != uiState.board.activePlayer) return
        uiState.setSelectedStack(StackOfReserveTile(selectEvent.reserveTile))
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectReserveCapStoneEvent) {
        if(selectEvent.player != uiState.board.activePlayer) return
        uiState.setSelectedStack(StackOfReserveCapStone(selectEvent.capStone))
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectStackEvent) {
        if(selectEvent.player != uiState.board.activePlayer) return
        //TODO
        frame.repaint()
    }

    @Synchronized
    fun onDeselect(deselectEvent: DeselectEvent) {
        //TODO
        frame.repaint()
    }

    @Synchronized
    fun onPlaceStack(placeStackEvent: PlaceStackEvent) {
        //TODO
        frame.repaint()
    }

    private fun executeMove(move: Move) {
        // Wrap it in a draw proposal (maybe)
        println("Executing $move")
        val result = uiState.board.execute(move)
        if (!result.isLegal) {
            println("Yeah actually $move was not legal")
        } else {
            uiState.apply(result)
        }
    }
}
