package org.marmotte.tak.notation

import org.marmotte.tak.engine.Board

interface TakPositionNotation {
    fun note(board: Board): String
}