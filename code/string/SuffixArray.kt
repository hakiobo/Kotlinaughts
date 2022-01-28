private fun constructSuffixArray(t: String): IntArray {
    val n = t.length
    val c = IntArray(max(n, 256))

    val rank = IntArray(n) { t[it].toInt() }
    val tempRank = IntArray(n)
    val suffix = IntArray(n) { it }
    val tempSuffix = IntArray(n)
    fun countingSort(k: Int) {
        var sum = 0
        c.fill(0)
        for (i in 0 until n) {
            c[(if (i + k < n) rank[i + k] else 0)]++
        }
        for (i in c.indices) {
            val temp = c[i]
            c[i] = sum
            sum += temp
        }
        for (i in 0 until n) {
            tempSuffix[c[if (suffix[i] + k < n) rank[suffix[i] + k] else 0]++] = suffix[i]
        }
        for (i in 0 until n) {
            suffix[i] = tempSuffix[i]
        }
    }

    var k = 1
    while (k < n) {
        countingSort(k)
        countingSort(0)
        tempRank[suffix[0]] = 0
        var r = 0
        for (i in 1 until n) {
            tempRank[suffix[i]] =
                if (rank[suffix[i]] == rank[suffix[i - 1]] && rank[suffix[i] + k] == rank[suffix[i - 1] + k]) r else ++r
        }
        for (i in 0 until n) {
            rank[i] = tempRank[i]
        }
        if (rank[suffix[n - 1]] == n - 1) break
        k = k shl 1
    }
    return suffix
}

private fun computeLCP(sa: IntArray, t: String): IntArray {
    val n = t.length
    val phi = IntArray(n)
    val plcp = IntArray(n)
    phi[sa[0]] = -1
    for (i in 1 until n) {
        phi[sa[i]] = sa[i - 1]
    }
    var l = 0
    for (i in 0 until n) {
        if (phi[i] == -1) continue
        while (t[i + l] == t[phi[i] + l]) l++
        plcp[i] = l
        l = max(l - 1, 0)
    }
    return IntArray(n) { plcp[sa[it]] }
}
