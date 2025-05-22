package org.marmotte.tak.display.parts

import java.awt.event.MouseEvent

data class TileEvent(val file: Int, val row: Int, val event: MouseEvent)
data class PromotionEvent(val file: Int, val row: Int, val event: MouseEvent)