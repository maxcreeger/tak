package org.marmotte.tak.notation

import org.marmotte.tak.engine.Board
import org.marmotte.tak.engine.Move

interface TakMoveNotation{
    fun note(game: List<Move>): String
    fun note(move: Move): String
    fun build(board: Board, notation: String): Move?
}