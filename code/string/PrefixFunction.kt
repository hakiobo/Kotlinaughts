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

private fun createAutomaton(s: String): Array<IntArray> {
    val aut = Array(s.length + 1) { IntArray(26) }
    val pi = prefixFunction(s)
    for (i in 0..s.length) {
        for (c in 0 until 26) {
            aut[i][c] = if (i == s.length || (i > 1 && 'a' + c != s[i])) {
                aut[pi[i - 1]][c]
            } else {
                i + if ('a' + c == s[i]) 1 else 0
            }
        }
    }
    return aut
}
