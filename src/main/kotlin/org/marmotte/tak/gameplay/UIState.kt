package org.marmotte.tak.gameplay

import org.marmotte.tak.engine.Board
import org.marmotte.tak.engine.MoveOutcome
import org.marmotte.tak.engine.Stack
import org.marmotte.tak.engine.StackMove

class UIState {

    var board: Board = Board.newGame(6)
        private set

    var hoveredStack: Stack? = null
        private set

    var selectedStack: Stack? = null
        private set

    var preparedMove: StackMove? = null

    private val illegalMoveListener = mutableListOf<(MoveOutcome) -> Unit>()
    private val moveListeners = mutableListOf<(MoveOutcome) -> Unit>()

    init {
        board.randomize()
    }

    fun apply(outcome: MoveOutcome) {
        if (!outcome.isLegal) {
            illegalMoveListener.forEach { it(outcome) }
            return
        }
        board = outcome.new
        hoveredStack = null
        selectedStack = null
        moveListeners.forEach { it(outcome) }
    }

    fun newGame(newBoard: Board = Board.newGame(5)) {
        board = newBoard
    }

    fun setHover(stack: Stack?) {
        hoveredStack = stack
    }

    fun setSelectedStack(stack: Stack?) {
        selectedStack = stack
    }

    fun addIllegalMoveListener(action: (MoveOutcome) -> Unit) {
        illegalMoveListener.add(action)
    }

    fun addMoveListener(action: (MoveOutcome) -> Unit) {
        moveListeners.add(action)
    }
}