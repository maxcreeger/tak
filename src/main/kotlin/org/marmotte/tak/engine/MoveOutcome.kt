package org.marmotte.tak.engine

data class MoveOutcome(
    val isLegal: Boolean = true,
    val old: Board,
    val move: Move,
    val new: Board,
)