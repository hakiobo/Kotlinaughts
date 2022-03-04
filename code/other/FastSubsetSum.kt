private fun fastSubsetSum(nums: IntArray, goal: Int, x: Int): Int {
    val n = nums.size
    val maxPref = IntArray((x shl 1) - 1) { -1 }
    val cur = IntArray(x)
    var part = 0
    var b = 0
    while (part + nums[b] <= goal) {
        part += nums[b]
        b++
    }
    maxPref[part - goal + x - 1] = b

    for (t in b until n) {
        if (maxPref[x - 1] >= 0) return goal
        for (mu in (x - 2) downTo 0) {
            maxPref[mu + nums[t]] = max(maxPref[mu + nums[t]], maxPref[mu])
        }
        for (mu in ((x-1) shl 1) downTo x) {
            var j = cur[mu - x]
            while (j < maxPref[mu]) {
                val mup = mu - nums[j]
                maxPref[mup] = max(maxPref[mup], j)
                j++
            }
            cur[mu - x] = j
        }
    }

    for (i in x - 1 downTo 0) {
        if (maxPref[i] >= 0) {
            return i + (goal - x + 1)
        }
    }
    return 0
}
