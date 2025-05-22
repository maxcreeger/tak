package org.marmotte.tak.gameplay

import org.marmotte.tak.controller.BoardController
import org.marmotte.tak.controller.GameMenu
import org.marmotte.tak.display.TakBoardPanel
import org.marmotte.tak.display.parts.RemainingPiecesPanel
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JPanel


class Display {

    companion object {
        const val DEFAULT_SCALE = 50
        const val MIN_SCALE = 20
        const val MAX_SCALE = 100
    }

    private val frame: JFrame
    private val uiState: UIState
    private val takBoardPanel: TakBoardPanel
    private val remainingPiecesPanel: RemainingPiecesPanel
    private val broker: BoardController
    private val menuBar: GameMenu


    init { // create models ===========================================
        uiState = UIState()
    }

    init { // Create views ============================================
        // Construct JFrame
        frame = JFrame("Chessy")
        // Chess Board
        takBoardPanel = TakBoardPanel(this.uiState)
        // Promotion Panel
        remainingPiecesPanel = RemainingPiecesPanel(this.uiState)
        val gluedProm = JPanel()
        gluedProm.add(remainingPiecesPanel)
        gluedProm.add(Box.createVerticalGlue())

        // ContentPane & layout
        val contentPane = frame.contentPane
        contentPane.layout = BoxLayout(contentPane, BoxLayout.X_AXIS)
        contentPane.add(takBoardPanel)
        contentPane.add(gluedProm)
    }

    init { // Create Controllers ======================================
        broker = BoardController(uiState, frame)
        menuBar = GameMenu(uiState, frame)
    }

    init { // Give control over UI ====================================
        takBoardPanel.addTileClickListener(broker)
        takBoardPanel.addTileMotionListener(broker)
        remainingPiecesPanel.addPlacementListener(broker)

        // Show
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.jMenuBar = menuBar
        frame.isVisible = true
        frame.pack()
    }
}