package org.marmotte.tak.display.events

import org.marmotte.tak.engine.Tower
import java.awt.event.MouseEvent

data class HoverEvent(val file: Int, val row: Int, val event: MouseEvent)
data class SelectReserveTileEvent(val player: Boolean, val event: MouseEvent)
data class SelectReserveCapStoneEvent(val player: Boolean, val event: MouseEvent)
data class SelectTowerEvent(val player: Boolean, val piece: Tower, val event: MouseEvent)
data class DeselectEvent(val player: Boolean, val event: MouseEvent)
data class PlaceRoadEvent(val file: Int, val row: Int, val event: MouseEvent)
data class PlaceWallEvent(val file: Int, val row: Int, val event: MouseEvent)
data class PlaceCapStoneEvent(val file: Int, val row: Int, val event: MouseEvent)
