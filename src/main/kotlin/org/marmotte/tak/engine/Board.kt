package org.marmotte.tak.engine

import kotlin.random.Random

class Board private constructor(
    val size: Int,
    val activePlayer: Boolean = true,
    /** File then row **/
    val board: List<List<Tower>>,
    nbReserveTilesWhite: Int,
    nbReserveTilesBlack: Int,
    nbReserveCapStonesWhite: Int,
    nbReserveCapStonesBlack: Int,
) {

    companion object {
        fun newGame(size: Int): Board {
            val (nbStones, nbCapstones) = when (size) {
                4 -> 15 to 0
                5 -> 21 to 1
                6 -> 30 to 1
                7 -> 40 to 2
                8 -> 40 to 2
                else -> throw UnsupportedOperationException()
            }
            val emptyTowers =
                List(size) { file ->
                    List(size) { row ->
                        Tower(Pos(file, row))
                    }
                }
            return Board(
                size,
                true,
                emptyTowers,
                nbStones,
                nbStones,
                nbCapstones,
                nbCapstones
            )
        }
    }


    val whiteReserve: Reserve = Reserve(true, nbReserveTilesWhite, nbReserveCapStonesWhite)
    val blackReserve: Reserve = Reserve(false, nbReserveTilesBlack, nbReserveCapStonesBlack)

    fun change(
        boardChanges: Map<Pos, Map<Int, Piece>>,
        consumedTile: Boolean = false,
        consumedCapStone: Boolean = false,
    ): Board {
        val new = List(size) { file ->
            List(size) { row ->
                val pos = Pos(file, row)
                var tower = towerAt(pos)!!
                boardChanges[pos]
                    ?.let { tower = tower.change(it) }
                tower
            }
        }
        return Board(
            size,
            !activePlayer,
            new,
            whiteReserve.tiles.size - if (consumedTile && activePlayer) 1 else 0,
            blackReserve.tiles.size - if (consumedTile && !activePlayer) 1 else 0,
            whiteReserve.capstones.size - if (consumedCapStone && activePlayer) 1 else 0,
            blackReserve.capstones.size - if (consumedCapStone && !activePlayer) 1 else 0,
        )
    }

    val status: GameStatus = GameStatus.ACTIVE // TODO

    fun randomize() {
        for (row in board) {
            for (tower in row) {
                val nbTiles = Random.nextInt(4) - 1
                when {
                    nbTiles < 0 -> tower.change(mapOf(nbTiles to Wall(Random.nextBoolean())))
                    nbTiles > 0 -> tower.change((0 until nbTiles).associateWith { Road(Random.nextBoolean()) })
                }
            }
        }
    }

    fun reserveOf(player: Boolean): Reserve = if (player) whiteReserve else blackReserve

    fun generateLegalMovesFrom(selected: Pos): List<Move> {
        return emptyList() // TODO
    }

    /** Returns the Tower at that [Pos] or null if pos is outside the board) */
    fun towerAt(pos: Pos): Tower? {
        return board.getOrNull(pos.file)?.getOrNull(pos.row)
    }

    fun execute(move: Move): MoveOutcome {
        return move.applyTo(this)
    }

    fun resign(): Move {
        TODO()
    }

    val towers: List<Tower> = board.flatMap { it }

}
