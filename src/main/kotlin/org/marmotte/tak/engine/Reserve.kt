package org.marmotte.tak.engine

class Reserve private constructor(val tiles: List<ReserveTile>, val capstones: List<CapStone>, val player: Boolean) {

    companion object {
        operator fun invoke(player: Boolean, nbStones: Int, nbCapStones: Int): Reserve {
            return Reserve(
                List(nbStones) { ReserveTile(player) },
                List(nbCapStones) { CapStone(player)},
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

    override fun toString(): String = "Reserve(${player.toPlayerName()}, ${tiles.size} tiles, ${capstones.size} capstones)"

}