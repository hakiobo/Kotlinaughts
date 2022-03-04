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

// this version allows you to construct the solution, but has worse space complexity
private fun fastSubsetSum(nums: IntArray, goal: Int, x: Int): BooleanArray {
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
    val prev = Array((x shl 1) - 1) { IntArray(b + 1) { -1 } }
    prev[part - goal + x - 1][b] = n

    for (t in b until n) {
        if (maxPref[x - 1] >= 0) break
        val s = nums[t]
        for (mu in (x - 2) downTo 0) {
            if (maxPref[mu] > maxPref[mu + s]) {
                prev[mu + s][maxPref[mu]] = t
                maxPref[mu + s] = maxPref[mu]
            }
        }
        for (mu in ((x - 1) shl 1) downTo x) {
            for (j in maxPref[mu] - 1 downTo cur[mu - x]) {
                val mup = mu - nums[j]
                if (maxPref[mup] < j) {
                    prev[mup][j] = j
                    maxPref[mup] = j
                }
            }
            cur[mu - x] = max(cur[mu - x], maxPref[mu])
        }
    }
    val used = BooleanArray(n)
    for (i in 0 until b) {
        used[i] = true
    }

    for (i in x - 1 downTo 0) {
        if (maxPref[i] >= 0) {
            var curSum = i
            var a = maxPref[i]
            var p = prev[curSum][a]
            while (p != n) {
                used[p] = p >= b
                curSum += nums[p] * if (p < b) {
                    a++
                    1
                } else -1


                while (prev[curSum][a] == -1) {
                    a++
                }
                p = prev[curSum][a]
            }
            return used
        }
    }
    return used
}
