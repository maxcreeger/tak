package org.marmotte.tak.notation

import org.marmotte.tak.engine.*

class TakPositionalSystem : TakPositionNotation {

    companion object {
        private val REGEX = Regex("""\[TPS "(\S+) (\d+) (\d+)"]\s*""")
    }

    override fun note(board: Board): String {
        val tps = board.tps()
        val turn = if (board.activePlayer) 1 else 2
        val move = board.moveNumber
        return """[TPS "$tps" $turn $move]"""
    }

    fun Board.tps(): String {
        val rows = (0 until size).map { row ->
            val rowDesc = mutableListOf<String>()
            var emptyTowers = 0
            for (fileNum in 0 until size) {
                val file = Pos.file(fileNum)
                val pos = Pos(file, row)
                val tower = towerAt(pos) ?: throw UnsupportedOperationException()
                if (tower.pieces.isEmpty()) {
                    emptyTowers++
                } else {
                    // dump previous 'empties'
                    if (emptyTowers > 0) {
                        rowDesc += if (emptyTowers == 1) {
                            "x"
                        } else {
                            "x$emptyTowers"
                        }
                        emptyTowers = 0
                    }
                    // write this tower
                    rowDesc += tower.tps()
                }
            }
            if (emptyTowers > 0) {
                rowDesc += if (emptyTowers == 1) {
                    "x"
                } else {
                    "x$emptyTowers"
                }
            }
            rowDesc.joinToString(",")
        }
        return rows.joinToString("/")
    }

    fun Tower.tps(): String {
        val tiles = pieces.joinToString("") { if (it.player) "1" else "2" }
        val top = when (topPiece) {
            is Wall -> "S"
            is CapStone -> "C"
            else -> ""
        }
        return tiles + top
    }

    override fun build(notation: String): Board? {
        val match = REGEX.matchEntire(notation) ?: return null
        val boardStr = match.groups[1]?.value ?: return null
        val playerTurn = match.groups[2]?.value?.toInt() ?: return null
        val moveNum = match.groups[3]?.value?.toInt() ?: return null
        val towers: List<List<Tower>> = boardStr
            .split("/")
            .mapIndexed { rowNum, rowStr ->
                val row = mutableListOf<Tower>()
                rowStr
                    .split(",")
                    .forEach { towerStr ->
                        val fileNum = row.size
                        if (towerStr.startsWith('x')) {
                            val nbEmpty = if (towerStr.length == 1) 1 else towerStr.substring(1).toInt()
                            repeat(nbEmpty) { i ->
                                val file = Pos.file(fileNum + i)
                                val pos = Pos(file, rowNum)
                                row.add(Tower(pos))
                            }
                        } else {
                            val file = Pos.file(fileNum)
                            val pos = Pos(file, rowNum)
                            val topStr = towerStr.last()
                            val (typeBuilder, colors) = when {
                                topStr.isDigit() -> ::Road to towerStr.toCharArray().toList()
                                topStr == 'S' -> ::Wall to towerStr.toCharArray().dropLast(1)
                                topStr == 'C' -> ::CapStone to towerStr.toCharArray().dropLast(1)
                                else -> return null
                            }
                            val stack = colors
                                .map { it == '1' }
                                .mapIndexed { height, playerNum ->
                                    if (height == colors.size - 1) {
                                        typeBuilder(playerNum)
                                    } else {
                                        Road(playerNum)
                                    }
                                }
                            row.add(Tower(pos, stack))
                        }
                    }
                row
            }
        val size = towers.size
        if(towers.any { it.size != size }) return null
        val stdReserve = Board.reserveForGameSize(size)
        val whiteTiles = towers
            .flatMap { it }
            .sumOf { tower -> tower.pieces.count { it.player && it.piece != PieceType.CAPSTONE} }
        val whiteCapStones = towers
            .flatMap { it }
            .sumOf { tower -> tower.pieces.count { it.player && it.piece == PieceType.CAPSTONE} }
        val blackTiles = towers
            .flatMap { it }
            .sumOf { tower -> tower.pieces.count { !it.player && it.piece != PieceType.CAPSTONE} }
        val blackCapStones = towers
            .flatMap { it }
            .sumOf { tower -> tower.pieces.count { !it.player && it.piece == PieceType.CAPSTONE} }
        return Board(
            towers.size,
            playerTurn == 1,
            towers,
            stdReserve.first - whiteTiles,
            stdReserve.first - blackTiles,
            stdReserve.second - whiteCapStones,
            stdReserve.second - blackCapStones,
            moveNum
        )
    }
}
