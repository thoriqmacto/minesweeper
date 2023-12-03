fun countUniqueChars(sequence: CharSequence): Int {
    // write your code here
    var uniqueChars = sequence.toSet()
    return uniqueChars.size
}