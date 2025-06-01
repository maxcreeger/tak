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

    fun isExhausted() = tiles.isEmpty() && capstones.isEmpty()

    override fun toString(): String = "Reserve(${player.toPlayerName()}, ${tiles.size} tiles, ${capstones.size} capstones)"

}