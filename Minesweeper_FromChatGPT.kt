import kotlin.random.Random

class Minesweeper(private val width: Int, private val height: Int, private val minesCount: Int) {
    private val board = Array(width) { Array(height) { Cell() } }

    init {
        placeMines()
        calculateAdjacentMines()
    }

    private fun placeMines() {
        val random = Random(System.currentTimeMillis())
        var minesToPlace = minesCount

        while (minesToPlace > 0) {
            val x = random.nextInt(width)
            val y = random.nextInt(height)

            if (!board[x][y].isMine) {
                board[x][y].isMine = true
                minesToPlace--
            }
        }
    }

    private fun calculateAdjacentMines() {
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (!board[x][y].isMine) {
                    val adjacentMines = countAdjacentMines(x, y)
                    board[x][y].adjacentMines = adjacentMines
                }
            }
        }
    }

    private fun countAdjacentMines(x: Int, y: Int): Int {
        var count = 0
        for (dx in -1..1) {
            for (dy in -1..1) {
                val newX = x + dx
                val newY = y + dy
                if (newX in 0 until width && newY in 0 until height && board[newX][newY].isMine) {
                    count++
                }
            }
        }
        return count
    }

    fun displayBoard() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                print(if (board[x][y].isRevealed) {
                    if (board[x][y].isMine) "* " else "${board[x][y].adjacentMines} "
                } else {
                    ". "
                })
            }
            println()
        }
    }

    fun revealCell(x: Int, y: Int) {
        if (x < 0 || x >= width || y < 0 || y >= height || board[x][y].isRevealed) {
            return
        }

        board[x][y].isRevealed = true

        if (board[x][y].isMine) {
            println("Game Over - You hit a mine!")
            displayBoard()
        } else if (board[x][y].adjacentMines == 0) {
            for (dx in -1..1) {
                for (dy in -1..1) {
                    revealCell(x + dx, y + dy)
                }
            }
        }
    }
}

class Cell(var isMine: Boolean = false, var isRevealed: Boolean = false, var adjacentMines: Int = 0)

fun main() {
    val minesweeper = Minesweeper(5, 5, 5)
    minesweeper.displayBoard()

    // Replace with user input or game logic for cell selection and revealing
    minesweeper.revealCell(2, 2)
}
