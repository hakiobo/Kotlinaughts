private fun longestIncreasingSubsequence(nums: IntArray): IntArray {
    val n = nums.size
    if (nums.isEmpty()) return IntArray(0)
    val prev = IntArray(n)
    val res = IntArray(n)
    var l = 0
    for (x in 0 until n) {
        var low = 0
        var high = l
        while (low < high) {
            val mid = (low + high) shr 1
            if (nums[x] > nums[res[mid]]) {
                low = mid + 1
            } else {
                high = mid
            }
        }
        if (low == l) l++
        res[low] = x
        prev[x] = if (low == 0) 0 else (res[low - 1])
    }
    var cur = res[l - 1]
    val ans = IntArray(l)
    for (x in l - 1 downTo 0) {
        ans[x] = cur
        cur = prev[cur]
    }
    return ans
}
