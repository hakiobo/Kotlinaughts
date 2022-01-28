private class SparseAndTable(nums: List<Long>) {
    val n = nums.size
    val table = List(
        if (Integer.highestOneBit(n) == n) Integer.numberOfTrailingZeros(n) + 1
        else Integer.numberOfTrailingZeros(Integer.highestOneBit(n - 1)) + 1
    ) { LongArray(n) }

    init {
        for (x in 0 until n) {
            table[0][x] = nums[x]
        }
        for (bit in 1 until table.size) {
            val pow = 1 shl bit
            var x = 0
            while (x + pow <= n) {
                table[bit][x] = table[bit - 1][x] and table[bit - 1][x + (pow shr 1)]
                x++
            }
        }
    }

    fun getANDRange(start: Int, end: Int): Long {
        return if (start <= end) {
            val dif = end - start + 1
            val high = Integer.highestOneBit(dif)
            val bit = Integer.numberOfTrailingZeros(high)
            if (high == dif) {
                table[bit][start]
            } else {
                table[bit][start] and table[bit][end - high + 1]
            }
        } else {
            getANDRange(start, n - 1) and getANDRange(0, end)
        }
    }
}