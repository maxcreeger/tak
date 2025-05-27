package org.marmotte.tak.gameplay

import org.marmotte.tak.engine.Board
import org.marmotte.tak.engine.MoveOutcome
import org.marmotte.tak.engine.Stack

class UIState {

    var board: Board = Board(8)
        private set

    var hoveredStack: Stack? = null
        private set

    var selectedStack: Stack? = null
        private set

    var isDrawRequestedOnNextMove: Boolean = false
        private set

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
        isDrawRequestedOnNextMove = false
        moveListeners.forEach { it(outcome) }
    }

    fun newGame() {
        board = Board(5)
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