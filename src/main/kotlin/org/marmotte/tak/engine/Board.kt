package org.marmotte.tak.engine

import kotlin.random.Random

class Board(val size: Int) {

    var activePlayer: Boolean = true
    val whiteReserve: Reserve = Reserve(true, size)
    val blackReserve: Reserve = Reserve(false, size)

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
                (1..Random.nextInt(3)).forEach { _ ->
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
        return move.applyTo(this)
    }

    fun resign(): Move {
        TODO()
    }

    val towers: List<Tower> = board.flatMap{ it }

}
