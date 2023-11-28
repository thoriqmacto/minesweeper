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

//    println("Original matrix:")
//    outputList.forEach { it ->
//        println(it.joinToString(""))
//    }
//    for (i in 0 until row){
//        val rowTemp = mutableListOf<Char>()
//        for (j in (0 until col)){
//            rowTemp += '.'
//            print(".")
//        }
//        outputList += rowTemp
//        println()
//    }

    minesCoordinate.forEach { it ->
        val coordinate = it.split("|").map { it.toInt() }
        val rowMine = coordinate[0]
        val colMine = coordinate[1]
//        println(coordinate)
        outputList[rowMine][colMine] = 'X'
//        println(outputList[rowMine][colMine])
    }

//    println("After update :")
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
//            println("$randomRow|$randomCol")
        }
    }while (output.size < numMines)

    return output
}