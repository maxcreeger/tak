package org.marmotte.tak.engine

class Reserve private constructor(val tiles: List<ReserveTile>, val capstones: List<CapStone>, val player: Boolean) {

    companion object {
        operator fun invoke(player: Boolean, boardSize: Int): Reserve {
            val (nbStones, nbCapstones) = when (boardSize) {
                4 -> 15 to 0
                5 -> 21 to 1
                6 -> 30 to 1
                7 -> 40 to 2
                8 -> 40 to 2
                else -> throw UnsupportedOperationException()
            }
            return Reserve(
                List(nbStones) { ReserveTile(player) },
                List(nbCapstones) { CapStone(player)},
                player,
            )
        }
    }

    fun pickTile(): Pair<Reserve, ReserveTile>? {
        val tile: ReserveTile = tiles.firstOrNull() ?: return null
        return Reserve(tiles.minus(tile), capstones, player) to tile
    }

    fun pickCapstone(): Pair<Reserve, CapStone>? {
        val capstone = capstones.firstOrNull() ?: return null
        return Reserve(tiles, capstones.minus(capstone), player) to capstone
    }

}