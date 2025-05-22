package org.marmotte.tak.engine

class Board(val size: Int) {

    var activePlayer: Boolean = true
    val whiteReserve = 30
    val blackReserve = 30
    private val board: List<List<Tower>> = List(size) {
        List(size) {
            Tower()
        }
    }
    val status: GameStatus = GameStatus.ACTIVE // TODO

    fun generateLegalMovesFrom(selected: Pos) : List<Move>{
        return emptyList() // TODO
    }

    fun pieceAt(tileNum: Pos) : Tower{
        return  board[tileNum.x][tileNum.y]
    }

    fun execute(move: Move): MoveOutcome {
        TODO()
    }

    fun resign(): Move {
        TODO()
    }

}
