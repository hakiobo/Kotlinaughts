private fun modPow(n: Long, k: Long, m: Long): Long {
    if (k == 0L) {
        return 1L
    }
    var half = modPow(n, k shr 1, m)
    half *= half
    if (k and 1L == 1L) {
        half %= m
        half *= n
    }
    return half % m
}