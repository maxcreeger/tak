package org.marmotte.tak.engine

data class MoveOutcome private constructor(
    val isLegal: Boolean = true,
    val old: Board,
    val move: Move,
    val new: Board,
) {
    companion object{

        operator fun invoke(old: Board, move: Move, new: Board): MoveOutcome {
            return MoveOutcome(true, old, move, new)
        }

        fun illegal(old: Board, move: Move, message: String): MoveOutcome {
            println("Illegal move: $message")
            return MoveOutcome(false, old, move, old)
        }
    }
}
