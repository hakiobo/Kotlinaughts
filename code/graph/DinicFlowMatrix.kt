private class DinicFlowMatrix(val n: Int) {
    private val capacities = Array(n) { LongArray(n) }
    private val flows = Array(n) { LongArray(n) }
    private val level = IntArray(n)

    fun addFlow(u: Int, v: Int, f1: Long) {
        capacities[u][v] += f1
    }

    private fun setUpLevelGraph(src: Int, snk: Int): Boolean {
        level.fill(-1)
        val queue = ArrayDeque<Int>()
        level[src] = 0
        queue += src
        while (queue.isNotEmpty()) {
            val node = queue.poll()
            for (dest in 0 until n) {
                if (capacities[node][dest] - flows[node][dest] > 0 && level[dest] == -1) {
                    level[dest] = level[node] + 1
                    if (dest == snk) return true
                    queue += dest
                }
            }
        }
        return false
    }

    private fun findBlockingFlow(node: Int, snk: Int, capacity: Long): Long {
        var available = capacity
        if (node == snk) return capacity
        for (dest in 0 until n) {
            if (available == 0L) break
            if (level[dest] == level[node] + 1 && flows[node][dest] < capacities[node][dest]) {
                val flow = findBlockingFlow(dest, snk, min(available, capacities[node][dest] - flows[node][dest]))
                available -= flow
                flows[node][dest] += flow
                flows[dest][node] -= flow
            }
        }
        if (available != 0L) level[node] = -1
        return capacity - available
    }

    fun maxFlow(src: Int, snk: Int): Long {
        var flow = 0L
        while (setUpLevelGraph(src, snk)) {
            flow += findBlockingFlow(src, snk, Long.MAX_VALUE)
        }
        return flow
    }
}