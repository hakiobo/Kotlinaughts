private class UF(val n: Int) {
    val reps = IntArray(n) { it }
    val rank = IntArray(n)
    fun find(a: Int): Int {
        var k = a
        var d = reps[a]
        while (d != k) {
            reps[k] = reps[d]
            k = d
            d = reps[k]
        }
        return k
    }

    fun union(a: Int, b: Int): Int {
        val u = find(a)
        val v = find(b)
        return if (u == v) {
            u
        } else if (rank[u] > rank[v]) {
            reps[v] = u
            u
        } else if (rank[u] < rank[v]) {
            reps[u] = v
            v
        } else {
            reps[v] = u
            rank[u]++
            u
        }
    }
}