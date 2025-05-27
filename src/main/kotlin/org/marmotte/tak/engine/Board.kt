package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import java.awt.BasicStroke
import java.awt.Graphics2D
import kotlin.random.Random

class Board(val size: Int) {

    var activePlayer: Boolean = true
    val whiteReserve: Reserve = Reserve(true)
    val blackReserve: Reserve = Reserve(false)

    /** File then row **/
    private val board: List<List<Tower>> = List(size) { file ->
        List(size) { row ->
            Tower(Pos(file, row))
        }
    }

    val status: GameStatus = GameStatus.ACTIVE // TODO

    fun randomize() {
        for(row in board) {
            for (tower in row) {
                for (i in 1..Random.nextInt(3)) {
                    tower.add(Road(Random.nextBoolean()))
                }
                if(tower.pieces().isEmpty() && Random.nextBoolean()) {
                    tower.add(Wall(Random.nextBoolean()))
                }
            }
        }
    }

    fun reserveOf(player: Boolean): Reserve = if (player) whiteReserve else blackReserve

    fun generateLegalMovesFrom(selected: Pos): List<Move> {
        return emptyList() // TODO
    }

    fun towerAt(pos: Pos): Tower? {
        return board.getOrNull(pos.row)?.getOrNull(pos.file)
    }

    fun execute(move: Move): MoveOutcome {
        TODO()
    }

    fun resign(): Move {
        TODO()
    }

    val towers: List<Tower> = board.flatMap{ it }

}
