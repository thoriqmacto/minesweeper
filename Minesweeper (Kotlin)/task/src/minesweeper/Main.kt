package minesweeper

import kotlin.random.Random

fun main() {
    val game = Minesweeper(9,9)

    while (!game.isFinish){
        val userMark = game.promptToMark()
        game.check(userMark)
    }
}

class Minesweeper(private val totalRow: Int, private val totalCol: Int){
    private var numMines = 0
    var isFinish = false

    private var outputList = mutableListOf<MutableList<Char>>()
    private var minesCoordinate = mutableListOf<Pair<Int,Int>>()
    private var guessCoordinate = mutableListOf<Pair<Int,Int>>()

    private val coordinateAlignment: (Int,Int) -> Pair<Int,Int> = { col,row ->
        Pair(col-1,row-1)
    }

    private val coordinateToPlacing: (Int,Int) -> Pair<Int,Int> = { col,row ->
        Pair(col+1,row+1)
    }

    init {
        print("How many mines do you want on the field? ")
        numMines = readln().toInt()
        outputList = MutableList(totalRow){MutableList(totalCol){'.'} }
        minesCoordinate = generateMinesCoordinate()

        placeMines(minesCoordinate)
        minesCoordinate.clear()
        constructBattlefield()
        print()
    }

    private fun constructBattlefield(){
        // add border and index for x and y coordinate
        val newRow = totalRow + 3
        val newCol = totalCol + 3
        val outputListWithBorder = MutableList(newRow) { MutableList(newCol) { '.' } }

        for (i in 0 until newRow) {
            for (j in 0 until newCol) {
                if (i == 0 && j > 1 && j != newCol - 1) {
                    outputListWithBorder[i][j] = (j - 1).digitToChar()
                } else if (j == 1 || j == newCol - 1) {
                    outputListWithBorder[i][j] = '|'
                } else if (j == 0 && i > 1 && i != newRow - 1) {
                    outputListWithBorder[i][j] = (i - 1).digitToChar()
                } else if (i == 1 || i == newRow - 1) {
                    outputListWithBorder[i][j] = '-'
                } else if ((i > 1 && i < newRow - 1) && (j > 1 && j < newCol - 1)) {
                    val currentChar = outputList[i - 2][j - 2]
                    if (currentChar == 'X') {
                        outputListWithBorder[i][j] = '.'

                        val (alignedCol,alignedRow) = coordinateAlignment(j,i)
                        minesCoordinate.add(Pair(alignedCol,alignedRow))
                    } else {
                        outputListWithBorder[i][j] = currentChar
                    }
                } else {
                    outputListWithBorder[i][j] = ' '
                }
            }
        }

        outputList = outputListWithBorder
    }

    private fun placeMines(minesCoordinate:MutableList<Pair<Int,Int>>) {
        minesCoordinate.forEach { (r,c) ->
            outputList[r][c] = 'X'

            // put number surround mines
            this.updateAdjacentCells(r, c)
        }
    }

    private fun generateMinesCoordinate():MutableList<Pair<Int,Int>>{
        val output = mutableListOf<Pair<Int,Int>>()

        do {
            val randomRow = Random.nextInt(0, totalRow)
            val randomCol = Random.nextInt(0, totalCol)

            if (!output.contains(Pair(randomRow,randomCol))) {
                output.add(Pair(randomRow,randomCol))
            }
        }while (output.size < numMines)

        return output
    }

    private fun updateAdjacentCells(rowMine: Int, colMine: Int) {
        val adjacentCells = mutableListOf<Pair<Int, Int>>()

        // Determine adjacent cells based on the mine position
        for (i in -1..1) {
            for (j in -1..1) {
                val newRow = rowMine + i
                val newCol = colMine + j

                if ((i != 0 || j != 0) && newRow in 0 until totalRow && newCol in 0 until totalCol) {
                    adjacentCells.add(Pair(newRow, newCol))
                }
            }
        }

        // Update adjacent cells
        adjacentCells.forEach { (r, c) ->
            val currentChar = outputList[r][c]
            if (currentChar.isDigit()) {
                var tempInt = currentChar.digitToInt()
                tempInt++
                outputList[r][c] = tempInt.digitToChar()
            }else if(currentChar != 'X' && currentChar == '.'){
                outputList[r][c] = '1'
            }
        }
    }

    private fun print(){
        outputList.forEach {
            println(it.joinToString(""))
        }
    }

    private fun isGuessedCoordinateEqual(mineList:MutableList<Pair<Int,Int>>,
                                         guessList:MutableList<Pair<Int,Int>>):Boolean{

        if (mineList.size != guessList.size) return false

        return mineList.toSet() == guessList.toSet()
    }

    fun promptToMark():String{
        print("Set/delete mines marks (x and y coordinates): > ")
        return readln()
    }

    private fun isContainNumber(coordinate:String):Boolean{
        val (col,row) = coordinate.split(" ").map { it.toInt() }
        val (placedCol,placedRow) = coordinateToPlacing(col,row)
        return outputList[placedRow][placedCol].isDigit()
    }

    private fun toggleCoordinate(coordinate: String){
        val (col,row) = coordinate.split(" ").map { it.toInt() }
        val (placedCol,placedRow) = coordinateToPlacing(col,row)
        val currentChar = outputList[placedRow][placedCol]

        if (currentChar == '*') {
            outputList[placedRow][placedCol] = '.'
            guessCoordinate.remove(Pair(col,row))
        } else {
            outputList[placedRow][placedCol] = '*'
            guessCoordinate.add(Pair(col,row))
        }
    }

    fun check(guessedCoordinate:String){
        if (isContainNumber(guessedCoordinate)){
            println("There is a number here")
            print("Set/delete mines marks (x and y coordinates):")
        }else{
            toggleCoordinate(guessedCoordinate)
            print()
        }
//        println(minesCoordinate)
//        println(guessCoordinate)

        isFinish = isGuessedCoordinateEqual(minesCoordinate,guessCoordinate)

        if (isFinish) println("Congratulations! You found all the mines!")
    }
}
