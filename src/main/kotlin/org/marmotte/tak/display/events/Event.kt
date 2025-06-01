package org.marmotte.tak.display.events

import org.marmotte.tak.engine.*

data class HoveredTowerEvent(val pos: Pos, val hoveredStack: StackOfPartialTower)
data class HoveredReserveEvent(val hoveredStack: Stack)

data class SelectReserveTileEvent(val reserveTile: ReserveTile)
data class SelectReserveCapStoneEvent(val player: Boolean, val capStone: CapStone)
data class SelectStackEvent(val player: Boolean, val stack: StackOfPartialTower)

data class DeselectEvent(val player: Boolean)
