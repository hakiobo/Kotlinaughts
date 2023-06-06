private class SparseAndTable(nums: LongArray) {
    val n = nums.size
    val table = Array(n.takeHighestOneBit().countTrailingZeroBits() + 1) { LongArray(n) }

    init {
        for (x in 0 until n) table[0][x] = nums[x]
        for (bit in 1 until table.size) {
            for (x in 0..n - (1 shl bit)) {
                table[bit][x] = table[bit - 1][x] and table[bit - 1][x + (1 shl (bit - 1))]
            }
        }
    }

    fun getANDRange(start: Int, end: Int): Long {
        return if (start <= end) {
            val dif = end - start + 1
            val high = dif.takeHighestOneBit()
            val bit = high.countTrailingZeroBits()
            table[bit][start] and table[bit][end - high + 1]
        } else 0

    }
}
