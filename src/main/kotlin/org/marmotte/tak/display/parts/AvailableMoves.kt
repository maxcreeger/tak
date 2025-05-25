package org.marmotte.tak.display.parts

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import org.marmotte.tak.engine.StackOfPartialTower
import org.marmotte.tak.engine.StackOfReserveCapStone
import org.marmotte.tak.engine.StackOfReserveTile
import org.marmotte.tak.gameplay.UIState
import java.awt.Graphics2D

class AvailableMoves(private val uiState: UIState) : Drawable {

    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        when (val selected = uiState.selectedStack) {
            null -> return
            is StackOfPartialTower -> {
                val legalMoves = uiState.board.generateLegalMovesFrom(selected.tower.pos)
                legalMoves.forEach { it.draw(g, updateContext) }
            }

            is StackOfReserveCapStone -> {
                //TODO
            }
            is StackOfReserveTile ->  {
                //TODO
            }
        }
    }
}