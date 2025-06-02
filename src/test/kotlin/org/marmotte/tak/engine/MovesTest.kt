package org.marmotte.tak.engine

import kotlin.test.Test
import kotlin.test.assertTrue

class MovesTest {

    /**
     * [TPS "x5/122x4/x5/x5/x5 1 1"]
     */
    @Test
    fun testStackMove() {
        // Prepare data
        val size = 5
        val mainPos = Pos('B', 2)
        val secondPos = mainPos.move(Dir.EAST)
        val mainTower = Tower(mainPos, listOf(Road(false), Road(true)))
        val secondaryTower = Tower(secondPos, listOf(Road(true)))
        val towers = List(size) { row ->
            List(size) { fileNum ->
                if (row == 2)
                    if (fileNum == 1) {
                        return@List mainTower
                    } else if (fileNum == 2){
                        return@List secondaryTower
                    }
                Tower(Pos(Pos.file(fileNum), row), emptyList())
            }
        }
        val board = Board(5, true, towers, 20, 21, 1, 1, 0)
        val move = StackMove(true, StackOfPartialTower(mainTower, mainTower.first()), mainTower.pos, Dir.EAST, listOf(1, 1))
        // Perform
        val outcome = board.execute(move)
        // Verify
        assertTrue { outcome.isLegal }
        assertTrue { outcome.new.towerAt(mainTower.pos)?.isEmpty() == true}
        val secondaryIncreased = outcome.new.towerAt(secondaryTower.pos) ?: throw AssertionError()
        assertTrue { secondaryIncreased.size == 2}
        val thirdPos = mainPos.move(Dir.EAST, 2)
        val thirdIncreased = outcome.new.towerAt(thirdPos) ?: throw AssertionError()
        assertTrue { thirdIncreased.size == 1}
    }
}
