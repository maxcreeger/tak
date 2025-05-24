package org.marmotte.tak.controller

import org.marmotte.tak.display.events.*
import org.marmotte.tak.engine.*
import org.marmotte.tak.gameplay.UIState
import javax.swing.JFrame

class BoardController(
    private val uiState: UIState,
    private val frame: JFrame,
) {

    private var selected: Piece? = null

    @Synchronized
    fun onHover(hoverEvent: HoverEvent) {
        if (hoverEvent.row !in 0..5 || hoverEvent.file !in 0..5) {
            uiState.setHover(null)
        } else {
            uiState.setHover(Pos(hoverEvent.row, hoverEvent.file))
        }
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectReserveTileEvent) {
        if(selectEvent.player != uiState.board.activePlayer) return
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectReserveCapStoneEvent) {
        if(selectEvent.player != uiState.board.activePlayer) return
        //TODO
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectTowerEvent) {
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
    fun onPlaceRoad(placeRoadEvent: PlaceRoadEvent) {
        //TODO
        frame.repaint()
    }

    @Synchronized
    fun onPlaceWall(placeWallEvent: PlaceWallEvent) {
        //TODO
        frame.repaint()
    }

    @Synchronized
    fun onPlaceCapStone(placeCapStoneEvent: PlaceCapStoneEvent) {
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
