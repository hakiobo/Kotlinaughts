//i make no guarantess about the accuracy of this code
private class MCMM(private val d: Int, private val costs: Array<DoubleArray>) {
    companion object {
        const val EPSILON = 1E-9
    }

    private val n = 2 + (d shl 1)
    private val src = n - 1
    private val snk = n - 2

    private val prices = DoubleArray(n) { 0.0 }

    private val usedFromSrc = BooleanArray(d)
    private val usedToSnk = BooleanArray(d)
    private val used = Array(d) { BooleanArray(d) }

    init {
        var low = costs[0][0]
        for (nfId in 0 until d) {
            prices[nfId + d] = costs[0][nfId]
            for (fId in 1 until d) {
                prices[nfId + d] = min(prices[nfId + d], costs[fId][nfId])
            }
            low = min(low, prices[nfId])
        }
        prices[snk] = low
    }

    fun findPath(): Double {
        val pq = PriorityQueue<Pair<Double, Int>> { a, b -> a.first.compareTo(b.first) }
        val dists = DoubleArray(n) { Double.MAX_VALUE }
        val prev = IntArray(n) { -1 }
        dists[src] = 0.0
        pq += 0.0 to src
        while (pq.isNotEmpty()) {
            val (cost, node) = pq.poll()
            if (cost == dists[node]) {
                when (node) {
                    src -> {
                        for (dest in 0 until d) {
                            if (!usedFromSrc[dest]) {
                                val newCost = cost + prices[node] - prices[dest]
                                if (newCost + EPSILON < dists[dest]) {
                                    prev[dest] = node
                                    dists[dest] = newCost
                                    pq += newCost to dest
                                }
                            }
                        }
                    }
                    snk -> {
                        for (nfId in 0 until d) {
                            val dest = d + nfId
                            if (usedToSnk[nfId]) {
                                val newCost = cost + prices[node] - prices[dest]
                                if (newCost + EPSILON < dists[dest]) {
                                    prev[dest] = node
                                    dists[dest] = newCost
                                    pq += newCost to dest
                                }
                            }
                        }
                    }
                    in 0 until d -> {
                        for (nfId in 0 until d) {
                            val dest = nfId + d
                            if (!used[node][nfId]) {
                                val newCost = cost + prices[node] + costs[node][nfId] - prices[dest]
                                if (newCost + EPSILON < dists[dest]) {
                                    prev[dest] = node
                                    dists[dest] = newCost
                                    pq += newCost to dest
                                }
                            }
                        }
                    }
                    else -> {
                        val nfId = node - d
                        for (dest in 0 until d) {
                            if (used[dest][nfId]) {
                                val newCost = cost + prices[node] - costs[dest][nfId] - prices[dest]
                                if (newCost + EPSILON < dists[dest]) {
                                    prev[dest] = node
                                    dists[dest] = newCost
                                    pq += newCost to dest
                                }
                            }
                        }
                        if (!usedToSnk[nfId]) {
                            val newCost = cost + prices[node] - prices[snk]
                            if (newCost + EPSILON < dists[snk]) {
                                prev[snk] = node
                                dists[snk] = newCost
                                pq += newCost to snk
                            }
                        }
                    }
                }
            }
        }
        var cost = 0.0
        if (dists[snk] == Double.MAX_VALUE) {
            exitProcess(-4)
        } else {
            var cn = snk
            while (prev[cn] != -1) {
                val pn = prev[cn]
                when (pn) {
                    src -> {
                        usedFromSrc[cn] = true
                    }
                    in 0 until d -> {
                        used[pn][cn - d] = true
                        cost += costs[pn][cn - d]
                    }
                    else -> {
                        if (cn == snk) {
                            usedToSnk[pn - d] = true
                        } else {
                            used[cn][pn - d] = false
                            cost -= costs[cn][pn - d]
                        }
                    }
                }
                cn = pn
            }
            for (x in 0 until n) {
                prices[x] += dists[x]
            }
            return cost
        }
    }
}