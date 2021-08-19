private fun prefixFunction(s: String): IntArray {
    val pi = IntArray(s.length)
    for (i in 1 until s.length) {
        var j = pi[i - 1]
        while (j > 0 && s[i] != s[j]) j = pi[j - 1]
        if (s[i] == s[j]) j++
        pi[i] = j
    }
    return pi
}