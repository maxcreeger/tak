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
            val emptyTowers = List(size) { file ->
                List(size) { row ->
                    Tower(Pos(file, row))
                }
            }
            return Board(
                size, true, emptyTowers, nbStones, nbStones, nbCapstones, nbCapstones
            )
        }
    }


    val whiteReserve: Reserve = Reserve(true, nbReserveTilesWhite, nbReserveCapStonesWhite)
    val blackReserve: Reserve = Reserve(false, nbReserveTilesBlack, nbReserveCapStonesBlack)

    /** Apply th provided changes.
     * @param towerCut: the [Pos] and height at which to cut off stacks
     * @param towerAdds: The [Pos] and [Piece]s to add on top of existing stacks
     * @param consumedTile: if `true`, will use a Tile from the reserve
     * @param consumedCapStone: if `true`, will use a [CapStone] from the reserve
     */
    fun change(
        towerCut: Map<Pos, Int>,
        towerAdds: Map<Pos, List<Piece>>,
        consumedTile: Boolean = false,
        consumedCapStone: Boolean = false,
    ): Board {
        val new = List(size) { file ->
            List(size) { row ->
                val pos = Pos(file, row)
                var tower = towerAt(pos)!!
                // Trimming the top off first
                val cut = towerCut[pos]
                if (cut != null) {
                    tower = tower.cutFrom(cut)
                }
                // Then adding the new pieces
                val adds = towerAdds[pos]
                if (adds != null) {
                    tower = tower.add(adds)
                }
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

    fun Pos.neighbours(player: Boolean): List<Pos> {
        return listOf(
            Pair(0, 1),
            Pair(1, 0),
            Pair(-1, 0),
            Pair(0, -1)
        )
            .mapNotNull { (dRow, dFile) ->
                towerAt(Pos(file + dFile, row + dRow))
            }
            .filter { it.topPiece?.player == player } // must be owned by the requested player
            .filter { it.topPiece?.piece != PieceType.WALL } // Capstones & Roads count
            .map { it.pos }
    }

    fun checkConnectivity(player: Boolean, source: List<Pos>, target: List<Pos>): Boolean {
        val open = source.toMutableSet()
        val closed = mutableSetOf<Pos>()
        while (open.isNotEmpty()) {
            val candidate = open.first().also { open.remove(it) }
            if (target.contains(candidate)) return true
            closed.add(candidate)
            val neighbours = candidate.neighbours(player)
            open.addAll(neighbours.minus(closed))
        }
        return false
    }

    val status: GameStatus by lazy {
        // Detect a path
        for (player in listOf(!activePlayer, activePlayer)) {
            // If a player makes a single move that creates a road for both players, then the player who made the move wins. So we start checking if the previous player won
            val firstRow = board[0].filter { it.topPiece?.player == player }.map { it.pos }
            val lastRow = board[board.size - 1].filter { it.topPiece?.player == player }.map { it.pos }
            if (checkConnectivity(player, firstRow, lastRow)) {
                return@lazy if (player) GameStatus.WHITE_WIN else GameStatus.BLACK_WIN
            }
            val firstCol = board.map { it[0] }.filter { it.topPiece?.player == player }.map { it.pos }
            val lastCol = board.map { it[it.size - 1] }.filter { it.topPiece?.player == player }.map { it.pos }
            if (checkConnectivity(player, firstCol, lastCol)) {
                return@lazy if (player) GameStatus.WHITE_WIN else GameStatus.BLACK_WIN
            }
        }

        // detect board is filled or Reserve is exhausted
        if (towers.all { it.pieces.isNotEmpty() } ||whiteReserve.isExhausted() ||blackReserve.isExhausted()) {
            // Count flat stones
            val flats = towers.mapNotNull { it.topPiece }.filterIsInstance<Road>() // Only visible Roads count
            val whiteFlats = flats.count { it.player }
            val blackFlats = flats.count { !it.player }
            return@lazy if (whiteFlats > blackFlats) {
                GameStatus.WHITE_WIN
            } else if (whiteFlats < blackFlats) {
                GameStatus.BLACK_WIN
            } else {
                GameStatus.DRAW
            }
        }
        GameStatus.ACTIVE
    }

    fun randomize() {
        for (row in board) {
            for (tower in row) {
                val nbTiles = Random.nextInt(4) - 1
                when {
                    nbTiles < 0 -> tower.add(listOf(Wall(Random.nextBoolean())))
                    nbTiles > 0 -> tower.add(List(nbTiles) { Road(Random.nextBoolean()) })
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
        if(status.isActive()) {
            return move.applyTo(this)
        } else {
            return MoveOutcome.illegal(this, move, "Game has ended with: $status")
        }
    }

    fun resign(): Move {
        TODO()
    }

    val towers: List<Tower> = board.flatMap { it }

}
