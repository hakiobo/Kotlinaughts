import kotlin.math.min

private class TopDownLazySegTree(nArg: Int) {
    private val n = ((nArg shl 1) - 1).takeHighestOneBit()

    // default value here, or do initialization in init block
    private val tree = IntArray(n shl 1) { Int.MAX_VALUE }
    private val updates = IntArray(n shl 1)


    fun update(begin: Int, end: Int, update: Int) { // default interpretation is addition
        update(begin, end, update, 1, 0, n - 1)
    }

    private fun update(beginTot: Int, endTot: Int, update: Int, id: Int, begin: Int, end: Int) {
        propogateUpdate(id)
        // no overlap
        if (endTot < begin || beginTot > end) return
        // complete overlap
        if (end <= endTot && begin >= beginTot) {
            updates[id] += update
            propogateUpdate(id)
            return
        }
        // partial overlap
        val d = (begin + end) shr 1
        // update children
        update(beginTot, endTot, update, id.left(), begin, d)
        update(beginTot, endTot, update, id.right(), d + 1, end)

        // use updated children to get new values
        tree[id] = min(tree[id.left()], tree[id.right()])
    }

    fun get(begin: Int, end: Int): Int {
        return get(begin, end, 1, 0, n - 1)
    }

    private fun get(beginTot: Int, endTot: Int, id: Int, begin: Int, end: Int): Int {
        propogateUpdate(id)
        // no overlap, including some default value
        if (endTot < begin || beginTot > end) return Int.MAX_VALUE
        // complete overlap
        if (end <= endTot && begin >= beginTot) return tree[id]

        // partial overlap
        val d = (begin + end) shr 1
        // get from children
        val lMin = get(beginTot, endTot, id.left(), begin, d)
        val rMin = get(beginTot, endTot, id.right(), d + 1, end)

        // combine children results
        return min(lMin, rMin)
    }

    private fun propogateUpdate(id: Int) {
        if (updates[id] != 0) {
            tree[id] += updates[id]
            if (id < n) {
                updates[id.left()] += updates[id]
                updates[id.right()] += updates[id]
            }
            updates[id] = 0
        }
    }

    private fun Int.right() = (this shl 1) + 1
    private fun Int.left() = this shl 1
}
