package org.marmotte.tak.controller

import org.marmotte.tak.display.events.*
import org.marmotte.tak.engine.Move
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
        println("selecting $selectEvent")
        uiState.setSelectedStack(StackOfReserveTile(selectEvent.reserveTile))
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectReserveCapStoneEvent) {
        if (selectEvent.player != uiState.board.activePlayer) return
        println("selecting $selectEvent")
        uiState.setSelectedStack(StackOfReserveCapStone(selectEvent.capStone))
        frame.repaint()
    }

    @Synchronized
    fun onSelect(selectEvent: SelectStackEvent) {
        if (selectEvent.player != uiState.board.activePlayer) return
        println("selecting $selectEvent")
        uiState.setSelectedStack(selectEvent.stack)
        frame.repaint()
    }

    @Synchronized
    fun onDeselect(deselectEvent: DeselectEvent) {
        uiState.setSelectedStack(null)
        println("Deselecting $deselectEvent")
        frame.repaint()
    }

    @Synchronized
    fun onPlaceStack(moveStackEvent: MoveStackEvent) {
        TODO()
        frame.repaint()
    }

    @Synchronized
    fun onPlaceNewRoad(newRoadEvent: PlaceReserveRoadEvent) {
        TODO()
        frame.repaint()
    }

    @Synchronized
    fun onPlaceNewWall(newWallEvent: PlaceReserveWallEvent) {
        TODO()
        frame.repaint()
    }

    @Synchronized
    fun onPlaceNewCapStone(newCapStoneEvent: PlaceCapStoneEvent) {
        TODO()
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
