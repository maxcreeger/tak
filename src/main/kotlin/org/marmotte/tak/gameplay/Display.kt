package org.marmotte.tak.gameplay

import org.marmotte.tak.controller.BoardController
import org.marmotte.tak.controller.GameMenu
import org.marmotte.tak.display.RemainingPiecesPanel
import org.marmotte.tak.display.TakBoardPanel
import javax.swing.*


class Display {

    companion object {
        const val DEFAULT_SCALE = 50
        const val MIN_SCALE = 20
        const val MAX_SCALE = 300
    }

    private val frame: JFrame
    private val uiState: UIState
    private val takBoardPanel: TakBoardPanel
    private val remainingPiecesPanelWhite: RemainingPiecesPanel
    private val remainingPiecesPanelBlack: RemainingPiecesPanel
    private val broker: BoardController
    private val menuBar: GameMenu


    init { // create models ===========================================
        uiState = UIState()
    }
    init { // Construct JFrame ============================================
        frame = JFrame("Tak")
    }

    init { // Create Controllers ======================================
        broker = BoardController(uiState, frame)
        menuBar = GameMenu(uiState, frame)

    }

    init { // Create views ============================================
        // Board
        takBoardPanel = TakBoardPanel(this.uiState, broker)
        // Remaining Panels
        remainingPiecesPanelWhite = RemainingPiecesPanel(this.uiState, true)
        remainingPiecesPanelBlack = RemainingPiecesPanel(this.uiState, false)

        // Layout
        val gluedWhite = JPanel().also {
            it.add(remainingPiecesPanelWhite)
            it.add(Box.createVerticalGlue())
        }
        val gluedBlack = JPanel().also {
            it.add(remainingPiecesPanelBlack)
            it.add(Box.createVerticalGlue())
        }

        // ContentPane & layout
        val contentPane = frame.contentPane
        contentPane.layout = BoxLayout(contentPane, BoxLayout.X_AXIS)
        contentPane.add(gluedWhite)
        contentPane.add(takBoardPanel)
        contentPane.add(gluedBlack)
    }

    init { // Give control over UI ====================================
        remainingPiecesPanelWhite.addBoardController(broker)
        remainingPiecesPanelBlack.addBoardController(broker)
        uiState.addIllegalMoveListener {
            JOptionPane.showMessageDialog(
                frame,
                it.message,
                "Illegal Move",
                JOptionPane.NO_OPTION,
            )
        }
        uiState.addMoveListener {
            if(!it.new.status.isActive()) {
                JOptionPane.showMessageDialog(
                    frame,
                    "${it.new.status} !",
                    "Game Over",
                    JOptionPane.NO_OPTION,
                )

            }
        }

        // Show
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.jMenuBar = menuBar
        frame.isVisible = true
        frame.pack()
    }
}