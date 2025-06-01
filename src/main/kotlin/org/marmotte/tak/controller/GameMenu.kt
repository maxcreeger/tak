package org.marmotte.tak.controller

import org.marmotte.tak.engine.Resign
import org.marmotte.tak.gameplay.UIState
import org.marmotte.tak.notation.TakPositionalSystem
import java.awt.event.ActionEvent
import javax.swing.*

class GameMenu(val uiState: UIState, frame: JFrame) : JMenuBar() {

    init {
        JMenu("Game").also { menu ->
            menu.add(
                JMenuItem(
                    object : AbstractAction("New Game", UIManager.getIcon("FileChooser.newFolderIcon")) {
                        override fun actionPerformed(e: ActionEvent?) {
                            uiState.newGame()
                            frame.repaint()
                        }
                    }
                )
            )
            menu.add(
                JMenuItem(
                    object : AbstractAction("New Game from TPS...", UIManager.getIcon("FileChooser.newFolderIcon")) {
                        override fun actionPerformed(e: ActionEvent?) {
                            val tps = JOptionPane.showInputDialog(
                                frame,
                                "Enter a valid TPS (Tak Positional System) string",
                                "New Game from TPS",
                                JOptionPane.QUESTION_MESSAGE,
                            )
                            val newBoard = TakPositionalSystem().build(tps)
                            if (newBoard == null) {
                                println("Could not parse TPS string: $tps")
                            } else {
                                uiState.newGame(newBoard)
                            }
                        }
                    }
                )
            )
        }.also { add(it) }
        JMenu("Move").also { menu ->
            menu.add(
                JMenuItem(
                    object : AbstractAction("Resign") {
                        override fun actionPerformed(e: ActionEvent?) {
                            val resigned = uiState.board.execute(Resign(uiState.board.activePlayer))
                            uiState.apply(resigned)
                            uiState.board.status.winner()?.let { winner ->
                                JOptionPane.showMessageDialog(
                                    frame,
                                    "$winner has won!",
                                    "Game Over",
                                    JOptionPane.NO_OPTION,
                                )
                            }
                            frame.repaint()
                        }
                    }
                )
            )
        }.also { add(it) }
        JMenu("Position").also { menu ->
            menu.add(
                JMenuItem(
                    object : AbstractAction("Display TPS") {
                        override fun actionPerformed(e: ActionEvent?) {
                            val tps = TakPositionalSystem().note(uiState.board)
                            JOptionPane.showMessageDialog(
                                frame,
                                tps,
                                "Tak Positional System",
                                JOptionPane.NO_OPTION,
                            )
                        }
                    }
                )
            )
        }.also { add(it) }
    }
}