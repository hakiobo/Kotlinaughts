private class BottomUpSegTree(private val n: Int) {
    private val tree = IntArray(n shl 1) { Int.MAX_VALUE }

    fun setMin(index: Int, value: Int) { // minequals operation, tree[x] = min(tree[x], value)
        var cur = index + n
        while (cur != 0 && value < tree[cur]) {
            tree[cur] = value
            cur = cur shr 1
        }
    }

    fun getMin(begin: Int, end: Int): Int {
        var left = begin + n
        var right = end + n
        var ans = n
        while (left <= right) {
            if (left and 1 == 1) ans = min(ans, tree[left++])
            if (right and 1 == 0) ans = min(ans, tree[right--])
            left = left shr 1
            right = right shr 1
        }
        return ans
    }
}