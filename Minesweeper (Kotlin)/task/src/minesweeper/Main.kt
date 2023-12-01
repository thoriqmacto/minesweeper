package minesweeper

import kotlin.random.Random

fun main() {
    print("How many mines do you want on the field?")
    val numMines = readln().toInt()
    val battleField = createBattleField(9,9,numMines)
}

fun createBattleField(row:Int,col:Int,mines:Int): MutableList<MutableList<Char>> {
    val outputList = MutableList(row){MutableList(col){'.'} }
    val minesCoordinate = generateMinesCoordinate(mines,row,col)

    // placing mines
    minesCoordinate.forEach { it ->
        val coordinate = it.split("|").map { it.toInt() }
        val rowMine = coordinate[0]
        val colMine = coordinate[1]
        outputList[rowMine][colMine] = 'X'

        // put number surround mines
        updateAdjacentCells(row,col,rowMine,colMine,outputList)
    }

    // print all
    outputList.forEach { it ->
        println(it.joinToString(""))
    }

    return outputList
}

fun generateMinesCoordinate(numMines:Int,row:Int, col: Int):MutableList<String>{
    val output = mutableListOf<String>()

    do {
        val randomRow = Random.nextInt(0, row)
        val randomCol = Random.nextInt(0, col)

        if (!output.contains("$randomRow|$randomCol")) {
            output.add("$randomRow|$randomCol")
        }
    }while (output.size < numMines)

    return output
}

fun updateAdjacentCells(totalRow: Int, totalCol: Int,
                        rowMine: Int, colMine: Int,
                        outputList: MutableList<MutableList<Char>>) {
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
