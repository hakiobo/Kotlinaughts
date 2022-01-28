private class SparseMinTable(val nums: IntArray) {
    val n = nums.size
    val table = Array(
        Integer.numberOfTrailingZeros(Integer.highestOneBit((n shl 2) - 1))
    ) { IntArray(n) }

    init {
        for (x in 0 until n) {
            table[0][x] = x
        }
        for (bit in 1 until table.size) {
            val pow = 1 shl bit
            for (x in 0..n - pow) {
                val a = table[bit - 1][x]
                val b = table[bit - 1][x + (pow shr 1)]
                if (nums[a] <= nums[b]) {
                    table[bit][x] = a
                } else {
                    table[bit][x] = b
                }
            }
        }
    }

    fun getMinRange(start: Int, end: Int): Int {
        return if (start <= end) {
            val dif = end - start + 1
            val high = Integer.highestOneBit(dif)
            val bit = Integer.numberOfTrailingZeros(high)
            if (high == dif) {
                table[bit][start]
            } else {
                val a = table[bit][start]
                val b = table[bit][end - high + 1]
                if (nums[a] <= nums[b]) {
                    a
                } else {
                    b
                }
            }
        } else {
            -1
        }
    }
}