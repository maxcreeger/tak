package org.marmotte.tak.display

import org.marmotte.tak.display.parts.PromotionEvent

interface PromotionListener {
    fun onClick(tileEvent: PromotionEvent)
}