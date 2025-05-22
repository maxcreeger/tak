package org.marmotte.tak.display

import org.marmotte.tak.display.parts.TileEvent

interface TileMotionListener {
    fun onMove(tileEvent: TileEvent)
}