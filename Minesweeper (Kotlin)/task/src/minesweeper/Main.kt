package minesweeper

import kotlin.random.Random

fun main() {
    val game = Minesweeper(9,9)

    while (!game.isFinish){
        val userMark = game.promptToMark()
        val (nextCol, nextRow, nextGuess:String) = userMark.split(" ").map{ it }
        val (listCol,listRow) = game.coordinateToListIndex(nextCol.toInt(),nextRow.toInt())
        game.revealCell(listRow,listCol,nextGuess)

        if(!game.isFinish){
            game.printMineField()
            game.check()
        }
    }
}

class Minesweeper(private val totalRow: Int, private val totalCol: Int){
    private var numMines = 0
    private val rowField = totalRow + 3 // to consider border shifting in mineField
    private val colField = totalCol + 3 // to consider border shifting in mineField
    private var correctMineGuess = 0
    private var unexploredCells = 0
    var isFinish = false

    private var gridMines = mutableListOf<MutableList<Cell>>()
    private var minesCoordinate = mutableListOf<Pair<Int,Int>>()

    val coordinateToListIndex:(Int,Int) -> Pair<Int,Int> = { column, row ->
        Pair(column+1, row+1)
    }

    // Inner class representing each cell in the Minesweeper grid
    inner class Cell(var isMine: Boolean = false,
                     var isExplored: Boolean = false,
                     var adjacentMines: Int = 0,
                     var isMarked: Boolean = false)

    init {
        // prompt user to indicate total mines for the game
        print("How many mines do you want on the field? ")
        numMines = readln().toInt()

        // init outputList to fill with Cell object
        gridMines = MutableList(rowField){MutableList(colField){Cell()} }

        // fill the gridMineChar with initial char then print the grid
        printMineField()

        // prompt the user for initial guess
        val firstGuess = promptToMark()
        val (initCol, initRow, guess:String) = firstGuess.split(" ").map{ it }

        // place mines in the field
        placeMines(Pair(initCol.toInt(),initRow.toInt()))

        // auto-explore mines for first time
        val (listCol,listRow) = coordinateToListIndex(initCol.toInt(),initRow.toInt())
        revealCell(listRow,listCol,guess)
        printMineField()
    }

    private fun countWinningParameters(){
        // count unexplored cells
        var totalUnexploredCells = 0
        var totalCorrectMineGuess = 0
        for (i in 0 until rowField - 1){
            for (j in 0 until colField -1){
                val cell = gridMines[i][j]
                if (!cell.isExplored){
                    totalUnexploredCells++
                }

                if(cell.isMarked && cell.isMine){
                    totalCorrectMineGuess++
                }
            }
        }
        unexploredCells = totalUnexploredCells
        correctMineGuess = totalCorrectMineGuess
    }

    private fun placeMines(initCoordinate:Pair<Int,Int>) {
        var randomRow = 1
        var randomCol = 1

        while((minesCoordinate.size < numMines) && Pair(randomCol,randomRow) != initCoordinate) {
            randomRow = Random.nextInt(1, totalRow)
            randomCol = Random.nextInt(1, totalCol)

            if (!minesCoordinate.contains(Pair(randomCol,randomRow))) {
                minesCoordinate.add(Pair(randomCol,randomRow))
            }
        }

        minesCoordinate.forEach { (col,row) ->
            val (listCol,listRow) = coordinateToListIndex(col,row)
            val cell = gridMines[listRow][listCol]
            cell.isMine = true
            // cell.isExplored = true

            // put number surround mines
            updateAdjacentCells(listRow, listCol)
        }
    }

    private fun updateAdjacentCells(rowCheck: Int, colCheck: Int) {
        val adjacentCells = mutableListOf<Pair<Int, Int>>()

        // Determine adjacent cells based on the check cells position
        for (i in -1..1) {
            for (j in -1..1) {
                val newRow = rowCheck + i
                val newCol = colCheck + j

                if ((i != 0 || j != 0) && (newRow in 0 until rowField) && (newCol in 0 until colField)) {
                    adjacentCells.add(Pair(newRow, newCol))
                }
            }
        }

        // Update adjacent cells
        adjacentCells.forEach { (row, col) ->
            val cell = gridMines[row][col]
            if (!cell.isMine) {
                // cell.isExplored = true
                cell.adjacentMines++
            }
        }
    }

    fun printMineField(){
        for (i in 0 until rowField){
            for (j in 0 until colField){
                val cell = gridMines[i][j]
                if(i == 0 && j > 1 && j != colField - 1) {          // first-row
                    cell.isExplored = true
                    print((j-1).digitToChar())
                }else if (j == 0 && i > 1 && i != rowField - 1) {   // first-col
                    cell.isExplored = true
                    print((i-1).digitToChar())
                }else if (j == 1 || j == colField - 1) {            // second-col
                    cell.isExplored = true
                    print('|')
                    if (j == colField - 1) println()
                }else if (i == 1 || i == rowField - 1) {            // second-row
                    cell.isExplored = true
                    print('-')
                }else if( i== 0 && j == 0){                         // Coordinate (0,0)
                    cell.isExplored = true
                    print(' ')
                }else if (cell.isExplored){
                    if (cell.isMine) {
                        print('X')
                    } else if(cell.adjacentMines > 0 ) {
                        print(cell.adjacentMines.digitToChar())
                    } else{
                        print('/')
                    }
                } else {
                    if (cell.isMarked){
                        print('*')
                    }else {
                        print('.')
                    }
                }
            }
        }

        countWinningParameters()

//        println("Total Mines:$numMines")
//        println("Mines Coordinate:$minesCoordinate")
//        println("Total Correct Mine Guess:$correctMineGuess")
//        println("Total Unexplored Cells:$unexploredCells")
    }

    fun promptToMark():String{
        print("Set/unset mines marks or claim a cell as free: > ")
        return readln()
    }

    fun revealCell(row: Int, col: Int, guessWord:String = "free") {
        val cell = gridMines[row][col]

        if (cell.isExplored) {
            return
        } else if(guessWord == "mine") {
            cell.isMarked = !cell.isMarked
        } else{
            cell.isExplored = true
        }

        if (cell.isMine && guessWord == "free"){
            printMineField()
            println("You stepped on a mine and failed!")
            isFinish = true
        }else if(cell.adjacentMines == 0 && guessWord == "free"){
            for (dx in -1..1){
                for (dy in -1..1){
                    if ((row+dx in 2 until rowField-1) && (col+dy in 2 until colField-1)) {
                        revealCell(row + dx, col + dy)
                    }
                }
            }
        }
    }

    fun check(){
        countWinningParameters()

        if (correctMineGuess == numMines || unexploredCells == numMines){
            isFinish = true
        }

        if (isFinish) println("Congratulations! You found all the mines!");
    }
}
