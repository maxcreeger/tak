package org.marmotte.tak.controller

import org.marmotte.tak.gameplay.UIState
import org.marmotte.tak.notation.TakPositionalSystem
import java.awt.Color
import java.awt.event.ActionEvent
import javax.swing.*
import javax.swing.text.AttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext

class GameMenu(val uiState: UIState, frame: JFrame) : JMenuBar() {

    companion object {
        private val COLOR_SCALE = (-10..10).associateWith { Color.getHSBColor((it + 10) * 0.05f, 1.0f, 0.5f) }
        fun chooseColor(value: Int): Color {
            return COLOR_SCALE.getOrElse(value) { Color.BLACK }
        }
    }

    fun JTextPane.append(msg: String, color: Color) {
        val sc = StyleContext.getDefaultStyleContext()
        var aset: AttributeSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color)
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console")
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED)

        setCaretPosition(document.length)
        setCharacterAttributes(aset, false)
        replaceSelection(msg)
    }

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
                            if(newBoard == null) {
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
                            val resigned = uiState.board.execute(uiState.board.resign())
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