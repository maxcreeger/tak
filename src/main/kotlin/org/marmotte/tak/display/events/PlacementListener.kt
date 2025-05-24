package org.marmotte.tak.display.events

interface PlacementListener {
    fun onPlaceRoad(clickEvent: PlaceRoadEvent)
    fun onPlaceWall(clickEvent: PlaceWallEvent)
    fun onPlaceCapStone(clickEvent: PlaceCapStoneEvent)
}
