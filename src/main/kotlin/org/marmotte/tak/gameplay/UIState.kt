package org.marmotte.tak.gameplay

import org.marmotte.tak.engine.Board
import org.marmotte.tak.engine.MoveOutcome
import org.marmotte.tak.engine.Pos

class UIState {

    var board: Board = Board(5)
        private set

    var hoveredEnemyPiece: Pos? = null
        private set

    var pieceSelected: Pos? = null
        private set

    var isDrawRequestedOnNextMove: Boolean = false
        private set

    private val illegalMoveListener = mutableListOf<(MoveOutcome) -> Unit>()
    private val moveListeners = mutableListOf<(MoveOutcome) -> Unit>()

    fun apply(outcome: MoveOutcome) {
        if (!outcome.isLegal) {
            illegalMoveListener.forEach { it(outcome) }
            return
        }
        board = outcome.new
        hoveredEnemyPiece = null
        pieceSelected = null
        isDrawRequestedOnNextMove = false
        moveListeners.forEach { it(outcome) }
    }

    fun newGame() {
        board = Board(5)
    }

    fun setHover(pos: Pos?) {
        hoveredEnemyPiece = pos
    }

    fun select(pos: Pos?) {
        pieceSelected = pos
    }

    fun addIllegalMoveListener(action: (MoveOutcome) -> Unit) {
        illegalMoveListener.add(action)
    }

    fun addMoveListener(action: (MoveOutcome) -> Unit) {
        moveListeners.add(action)
    }
}