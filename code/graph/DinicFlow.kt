import java.util.ArrayDeque //edit once kattis switches to 1.4
import kotlin.math.min

private class DinicFlow(n: Int = 0) {
    private var numNodes = 0
    private val edges = MutableList(n) { mutableListOf<Int>() }
    private val dests = mutableListOf<Int>()
    private val capacities = mutableListOf<Long>()
    fun addNode(): Int {
        if (numNodes >= edges.size) {
            edges += mutableListOf<Int>()
        }
        return numNodes++
    }

    fun addEdge(u: Int, v: Int, f1: Long, f2: Long = 0L) {
        edges[u].add(dests.size)
        dests += v
        capacities += f1
        edges[v].add(dests.size)
        dests += u
        capacities += f2
    }

    private fun setUpLevelGraph(src: Int, snk: Int): IntArray? {
        val level = IntArray(edges.size) { -1 }
        val queue = ArrayDeque<Int>()
        level[src] = 0
        queue += src
        while (queue.isNotEmpty()) {
            val node = queue.poll()
            for (edge in edges[node]) {
                val dest = dests[edge]
                if (capacities[edge] > 0 && level[dest] == -1) {
                    level[dest] = level[node] + 1
                    if (dest == snk) return level
                    queue += dest
                }
            }
        }
        return null
    }

    private fun findBlockingFlow(node: Int, snk: Int, capacity: Long, level: IntArray): Long {
        var available = capacity
        if (node == snk) return capacity
        for (edge in edges[node]) {
            if (available == 0L) break
            val dest = dests[edge]
            if (level[dest] == level[node] + 1 && capacities[edge] != 0L) {
                val flow = findBlockingFlow(dest, snk, min(available, capacities[edge]), level)
                available -= flow
                capacities[edge] -= flow
                capacities[edge xor 1] += flow
            }
        }
        if (available != 0L) level[node] = -1
        return capacity - available
    }

    fun maxFlow(src: Int, snk: Int): Long {
        var flow = 0L
        var level = setUpLevelGraph(src, snk)
        while (level != null) {
            flow += findBlockingFlow(src, snk, Long.MAX_VALUE, level)
            level = setUpLevelGraph(src, snk)
        }
        return flow
    }
}