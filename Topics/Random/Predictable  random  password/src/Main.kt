import kotlin.random.Random

fun generatePredictablePassword(seed: Int): String {
    var randomPassword = ""
    val randomSeeding = Random(seed)
    // write your code here
    for (i in 0 until 10){
        val randomNum = randomSeeding.nextInt(33,127)
//        println(randomNum)
        randomPassword += randomNum.toChar()
    }
	return randomPassword
}

//fun main(){
//    val password = generatePredictablePassword(42)
//    println(password)
//}