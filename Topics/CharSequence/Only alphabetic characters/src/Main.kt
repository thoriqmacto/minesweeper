fun containsOnlyAlphabets(charSequence: CharSequence): Boolean {
    // write your code here
    return charSequence.matches(Regex("[a-zA-Z]+"))
}