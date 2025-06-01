package org.marmotte.tak.engine

import org.marmotte.tak.engine.GameStatus.*


/**
 * State of the Game
 *
 * * [ACTIVE]: currently undecided, one player is expected to move
 * * [ACTIVE_DRAW_REQUESTED]: currently undecided, one player is expected to move, but the previous has offered a draw
 *   that may be accepted
 * * [DRAW]: Game is a draw (draw offered was extended, and accepted by the other player)
 * * [BLACK_WIN]: Game is won by BLACK
 * * [WHITE_WIN]: Game is won by WHITE
 * * [FORFEIT]: Game has been lost by forfeit (illegal move played, time allocation exceeded)
 */
enum class GameStatus {
    ACTIVE, ACTIVE_DRAW_REQUESTED, DRAW, BLACK_WIN, WHITE_WIN, FORFEIT;

    fun isActive(): Boolean = this == ACTIVE || this == ACTIVE_DRAW_REQUESTED

    fun winner(): Boolean? = when(this) {
        BLACK_WIN -> false
        WHITE_WIN -> true
        else -> null
    }

}
