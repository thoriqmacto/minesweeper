import kotlin.random.Random

fun rpgDices(dice1: Int, dice2: Int): Int {
    // write your code here
    val rand1 = Random.nextInt(1,dice1+1)
    val rand2 = Random.nextInt(1,dice2+1)
    val sumOfDice = rand1 + rand2
//    println("$rand1,$rand2,$sumOfDice")
    return sumOfDice
}

//fun main(){
//    rpgDices(8,12)
//}