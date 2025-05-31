package org.marmotte.tak.notation

import org.marmotte.tak.engine.*
import org.marmotte.tak.engine.Dir.*
import org.marmotte.tak.notation.PTNDirStyle.*
import org.marmotte.tak.notation.PTNLength.FULL
import org.marmotte.tak.notation.PTNLength.SHORT
import org.marmotte.tak.notation.PTNPosStyle.LOWERCASE
import org.marmotte.tak.notation.PTNPosStyle.UPPERCASE

/** Portable Tak Notation, see https://ustak.org/portable-tak-notation/ **/
class PortableTakNotation(val styles: List<PTNStyle>) : TakMoveNotation {

    companion object {
        val NOTATION_PARSER = Regex("""([CBS\d]?)([a-h])([1-8])([<>+\-)])(\d+)""")
    }

    inner class NotationVisitor : MoveVisitor<Unit, String> {

        val lengthStyle: PTNLength = styles.filterIsInstance<PTNLength>().firstOrNull() ?: FULL
        val dirStyle: PTNDirStyle = styles.filterIsInstance<PTNDirStyle>().firstOrNull() ?: STANDARD
        val posStyle: PTNPosStyle = styles.filterIsInstance<PTNPosStyle>().firstOrNull() ?: LOWERCASE

        fun Pos.ptn(): String {
            return if (posStyle == UPPERCASE) {
                fileLetter().uppercaseChar() + row.toString()
            } else {
                fileLetter().lowercaseChar() + row.toString()
            }
        }

        fun Char?.toDir(): Dir? = when (this) {
            '-' -> NORTH
            '+' -> SOUTH
            '>' -> EAST
            '<' -> WEST
            '↑' -> NORTH
            '↓' -> SOUTH
            '→' -> EAST
            '←' -> WEST
            '^' -> NORTH
            'v' -> SOUTH
            else -> null
        }

        fun Dir.ptn(): Char = when (dirStyle) {
            STANDARD -> when (this) {
                NORTH -> '-'
                SOUTH -> '+'
                EAST -> '>'
                WEST -> '<'
            }

            ARROWS -> when (this) {
                NORTH -> '↑'
                SOUTH -> '↓'
                EAST -> '→'
                WEST -> '←'
            }

            CARET -> when (this) {
                NORTH -> '^'
                SOUTH -> 'v'
                EAST -> '>'
                WEST -> '<'
            }
        }

        override fun visit(move: StackMove, input: Unit): String {
            val posStr = move.pos.ptn()
            val dirStr = move.dir.ptn()
            val lengthStyle = styles.filterIsInstance<PTNLength>().firstOrNull() ?: FULL
            val dropsStr = if (lengthStyle == SHORT && move.distrib.size == 1) {
                ""
            } else {
                move.distrib.joinToString("")
            }
            return "${move.stack.size}$posStr$dirStr$dropsStr"
        }

        override fun visit(move: PlaceReserveWall, input: Unit): String {
            return "S${move.pos.ptn()}"
        }

        override fun visit(move: PlaceReserveRoad, input: Unit): String {
            return when (lengthStyle) {
                SHORT -> move.pos.ptn()
                FULL -> "F${move.pos.ptn()}"
            }
        }

        override fun visit(move: PlaceCapStone, input: Unit): String {
            return "C${move.pos.ptn()}"
        }


        fun build(board: Board, notation: String): Move? {
            val player = board.activePlayer
            val match = NOTATION_PARSER.matchEntire(notation) ?: return null
            val file = match.groups[2]?.value?.first() ?: return null
            val row = match.groups[3]?.value?.first()?.digitToInt() ?: return null
            val pos = Pos(file, row)
            val pieceType = match.groups[1]?.value?.first()
            return when {
                pieceType == null -> PlaceReserveRoad(player, pos)
                pieceType == 'C' -> PlaceCapStone(player, CapStone(player), pos)
                pieceType == 'S' -> PlaceReserveWall(player, pos)
                pieceType.isDigit() -> {
                    val nbPieces = pieceType.digitToInt()
                    val tower = board.towerAt(pos) ?: return null
                    val fromPieceHeight = tower.pieces.size - nbPieces
                    val fromPiece = tower.pieces[fromPieceHeight]
                    val dir = match.groups[4]?.value?.first().toDir() ?: return null
                    val distrib = match.groups[5]?.value?.toCharArray()?.map(Char::digitToInt) ?: listOf(nbPieces)
                    StackMove(
                        player,
                        StackOfPartialTower(tower, fromPiece),
                        pos,
                        dir,
                        distrib
                    )
                }

                else -> null
            }
        }

    }

    override fun note(move: Move): String {
        return move.accept(NotationVisitor(), Unit)
    }

    override fun note(game: List<Move>): String {
        return game
            .windowed(2, 2, true)
            .mapIndexed { moveNum, movesInTheTurn ->
                val whiteMove = movesInTheTurn.first().let(this::note)
                val blackMove = movesInTheTurn.getOrNull(2)?.let(this::note)
                if (blackMove != null) {
                    "$moveNum. $whiteMove $blackMove"
                } else {
                    "$moveNum. $whiteMove "
                }
            }.joinToString("\n")
    }


    override fun build(board: Board, notation: String): Move? {
        return NotationVisitor().build(board, notation)
    }
}

sealed interface PTNStyle

enum class PTNLength : PTNStyle {
    FULL, SHORT
}

enum class PTNDirStyle : PTNStyle {
    STANDARD, ARROWS, CARET
}

enum class PTNPosStyle : PTNStyle {
    LOWERCASE, UPPERCASE
}
