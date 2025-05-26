package org.marmotte.tak.display.events

import org.marmotte.tak.engine.CapStone
import org.marmotte.tak.engine.Dir
import org.marmotte.tak.engine.Piece
import org.marmotte.tak.engine.ReserveTile
import org.marmotte.tak.engine.Stack
import org.marmotte.tak.engine.StackOfPartialTower
import org.marmotte.tak.engine.Tower
import java.awt.event.MouseEvent

data class HoveredTowerEvent(val file: Int, val row: Int, val hoveredStack: StackOfPartialTower, val event: MouseEvent)
data class HoveredReserveEvent(val hoveredStack: Stack, val event: MouseEvent)
data class SelectReserveTileEvent(val reserveTile: ReserveTile, val event: MouseEvent)
data class SelectReserveCapStoneEvent(val player: Boolean, val capStone: CapStone, val event: MouseEvent)
data class SelectStackEvent(val player: Boolean, val stack: StackOfPartialTower, val event: MouseEvent)
data class DeselectEvent(val player: Boolean, val event: MouseEvent)
data class PlaceStackEvent(private val stack: Stack, val file: Int, val row: Int, val dir: Dir, val event: MouseEvent)
data class PlaceWallEvent(val file: Int, val row: Int, val event: MouseEvent)
data class PlaceCapStoneEvent(val file: Int, val row: Int, val event: MouseEvent)
