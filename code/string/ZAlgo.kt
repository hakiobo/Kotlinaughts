private fun z(s: String): IntArray {
    val z = IntArray(s.length)
    var x = 0
    var y = 0
    for (i in 1 until s.length) {
        z[i] = (z[i - x].coerceAtMost(y - i + 1)).coerceAtLeast(0)
        while (i + z[i] < s.length && s[z[i]] == s[i + z[i]]) {
            x = i
            y = i + z[i]
            z[i]++
        }
    }
    return z
}