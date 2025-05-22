package org.marmotte.tak.controller

import org.marmotte.tak.display.PromotionListener
import org.marmotte.tak.display.TileClickListener
import org.marmotte.tak.display.TileMotionListener
import org.marmotte.tak.display.parts.PromotionEvent
import org.marmotte.tak.display.parts.TileEvent
import org.marmotte.tak.engine.*
import org.marmotte.tak.gameplay.UIState
import javax.swing.JFrame

class BoardController(
    private val uiState: UIState,
    private val frame: JFrame,
) : TileClickListener, TileMotionListener, PromotionListener {

    @Synchronized
    override fun onMove(tileEvent: TileEvent) {
        if (tileEvent.row !in 0..5 || tileEvent.file !in 0..5) {
            uiState.setHover(null)
        } else {
            uiState.setHover(Pos(tileEvent.row, tileEvent.file))
        }
        frame.repaint()
    }

    @Synchronized
    override fun onClick(tileEvent: PromotionEvent) {
        //TODO
        frame.repaint()
    }

    @Synchronized
    override fun onClick(tileEvent: TileEvent) {
        //TODO
        frame.repaint()
    }

    private fun executeMove(move: Move) {
        // Wrap it in a draw proposal (maybe)
        println("Executing $move")
        val result = uiState.board.execute(move)
        if (!result.isLegal) {
            println("Yeah actually $move was not legal")
        } else {
            uiState.apply(result)
        }
    }
}