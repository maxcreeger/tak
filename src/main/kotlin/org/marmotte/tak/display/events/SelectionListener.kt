package org.marmotte.tak.display.events

interface SelectionListener {
    fun onSelect(clickEvent: SelectEvent)
    fun onDeselect(clickEvent: DeselectEvent)
}
