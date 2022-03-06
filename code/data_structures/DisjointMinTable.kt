private class DisjointMinTable(input: IntArray) {
    val n = Integer.highestOneBit((input.size shl 1) - 1)
    val levels = Integer.numberOfTrailingZeros(n)
    val nums = IntArray(n) { idx -> if (idx < input.size) input[idx] else 0 }
    val table = Array(levels) { IntArray(n) }

    init {
        for (level in 0 until levels) {
            for (block in 0 until (1 shl level)) {
                val start = block shl (levels - level)
                val end = (block + 1) shl (levels - level)
                val mid = (start + end) shr 1

                table[level][mid] = nums[mid]
                for (x in mid + 1 until end) {
                    table[level][x] = min(table[level][x - 1], nums[x])
                }

                table[level][mid - 1] = nums[mid - 1]
                for (x in mid - 2 downTo start) {
                    table[level][x] = min(table[level][x + 1], nums[x])
                }
            }
        }
    }

    fun getMinRange(start: Int, end: Int): Int {
        if (end < start) return getMinRange(end, start)
        if (start == end) return nums[start]
        val level = levels - 1 - Integer.numberOfTrailingZeros(Integer.highestOneBit(start xor end))
        return min(table[level][start], table[level][end])
    }
}
