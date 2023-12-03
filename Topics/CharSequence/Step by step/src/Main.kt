fun removeEveryOtherChar(input: CharSequence): CharSequence {
    // write your code here
    var sb = ""
    for (i in input.indices step 2){
        sb += input[i]
    }
    return sb
}

//fun main(){
//    val input = "Hello, world!"
//    val result = removeEveryOtherChar(input)
//    println(result)
//}