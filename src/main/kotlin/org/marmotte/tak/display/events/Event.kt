package org.marmotte.tak.display.events

import org.marmotte.tak.engine.CapStone
import org.marmotte.tak.engine.Dir
import org.marmotte.tak.engine.Pos
import org.marmotte.tak.engine.ReserveTile
import org.marmotte.tak.engine.Stack
import org.marmotte.tak.engine.StackOfPartialTower
import java.awt.event.MouseEvent

data class HoveredTowerEvent(val pos: Pos, val hoveredStack: StackOfPartialTower, val event: MouseEvent)
data class HoveredReserveEvent(val hoveredStack: Stack, val event: MouseEvent)

data class SelectReserveTileEvent(val reserveTile: ReserveTile, val event: MouseEvent)
data class SelectReserveCapStoneEvent(val player: Boolean, val capStone: CapStone, val event: MouseEvent)
data class SelectStackEvent(val player: Boolean, val stack: StackOfPartialTower, val event: MouseEvent)

data class DeselectEvent(val player: Boolean, val event: MouseEvent)

data class MoveStackEvent(val player: Boolean, private val stack: Stack, val pos: Pos, val dir: Dir, val event: MouseEvent)
data class PlaceReserveWallEvent(val player: Boolean, val pos: Pos, val event: MouseEvent)
data class PlaceReserveRoadEvent(val player: Boolean, val pos: Pos, val event: MouseEvent)
data class PlaceCapStoneEvent(val player: Boolean, val capStone: CapStone, val pos: Pos, val event: MouseEvent)
data class DestroyWallEvent(val player: Boolean, val capStone: CapStone, val pos: Pos, val event: MouseEvent)
