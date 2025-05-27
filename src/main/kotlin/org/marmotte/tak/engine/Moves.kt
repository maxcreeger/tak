package org.marmotte.tak.engine

import org.marmotte.tak.display.drawables.Drawable
import org.marmotte.tak.display.drawables.UpdateContext
import java.awt.Graphics2D

interface Move : Drawable {
    fun applyTo(board: Board): MoveOutcome

}

data class StackMove(val player: Boolean, private val stack: Stack, val pos: Pos, val dir: Dir): Move {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        TODO("Not yet implemented")
    }

    override fun applyTo(board: Board): MoveOutcome {
        TODO("Not yet implemented")
    }
}

data class PlaceReserveWall(val player: Boolean, val pos: Pos): Move {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        TODO("Not yet implemented")
    }

    override fun applyTo(board: Board): MoveOutcome {
        TODO("Not yet implemented")
    }
}

data class PlaceReserveRoad(val player: Boolean, val pos: Pos): Move {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        TODO("Not yet implemented")
    }

    override fun applyTo(board: Board): MoveOutcome {
        TODO("Not yet implemented")
    }
}

data class PlaceCapStone(val player: Boolean, val capStone: CapStone, val pos: Pos):Move {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        TODO("Not yet implemented")
    }

    override fun applyTo(board: Board): MoveOutcome {
        TODO("Not yet implemented")
    }
}

data class DestroyWall(val player: Boolean, val capStone: CapStone, val pos: Pos):Move {
    override fun draw(g: Graphics2D, updateContext: UpdateContext) {
        TODO("Not yet implemented")
    }

    override fun applyTo(board: Board): MoveOutcome {
        TODO("Not yet implemented")
    }
}