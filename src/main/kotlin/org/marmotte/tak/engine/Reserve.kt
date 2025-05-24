package org.marmotte.tak.engine

class Reserve private constructor(val tiles: List<ReserveTile>, val capstone: CapStone?, val player: Boolean) {

    constructor(player: Boolean) : this(
        List(10) { ReserveTile(player) },
        CapStone(player),
        player,
    )

    fun pickTile(): Pair<Reserve, ReserveTile>? {
        val tile: ReserveTile = tiles.firstOrNull() ?: return null
        return Reserve(tiles.minus(tile), capstone, player) to tile
    }

    fun pickCapstone(): Pair<Reserve, CapStone>? {
        if(capstone ==null) return null
        return Reserve(tiles, null, player) to capstone
    }

}