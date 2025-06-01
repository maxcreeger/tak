package org.marmotte.tak.engine

@ConsistentCopyVisibility
data class MoveOutcome private constructor(
    val isLegal: Boolean = true,
    val old: Board,
    val move: Move,
    val new: Board,
    val message: String
) {
    companion object{

        operator fun invoke(old: Board, move: Move, new: Board, message: String = "Legal move"): MoveOutcome {
            return MoveOutcome(true, old, move, new, message)
        }

        fun illegal(old: Board, move: Move, message: String): MoveOutcome {
            println("Illegal move: $message")
            return MoveOutcome(false, old, move, old, message)
        }
    }
}
