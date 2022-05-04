// https://open.kattis.com/problems/cordonbleu
// Min Cost Match Matching
private class Hungarian(val costs: Array<IntArray>) { // assuming a square matrix of costs
    val n = costs.size
    var adjustment = 0
    val labelByRow = IntArray(n)
    val labelByCol = IntArray(n)
    var nextRowToMatch = -1

    val minSlackRowByCol = IntArray(n)
    val minSlackValueByCol = IntArray(n)
    val committedRows = IntArray(n)
    var numCommittedRows = 0
    val parentRowByCommittedCol = IntArray(n)
    val matchColByRow = IntArray(n) { -1 }
    val matchRowByCol = IntArray(n) { -1 }

    fun execute(): Int { // returns the final cost, can get the arrangements from matchRowByCol an matchColByRow
        reduce()
        greedyMatch()

        var row = fetchUnmatchedRow()
        while (row != n) {
            initializePhase(row)
            executePhase()
            row = fetchUnmatchedRow()
        }
        return adjustment
    }

    inline fun initializePhase(row: Int) {
        numCommittedRows = 0
        parentRowByCommittedCol.fill(-1)
        committedRows[numCommittedRows++] = row
        for (col in 0 until n) {
            minSlackValueByCol[col] = costs[row][col] - labelByRow[row] - labelByCol[col]
            minSlackRowByCol[col] = row
        }
    }

    inline fun executePhase() {
        while (true) {
            var minSlackRow = -1
            var minSlackCol = -1
            var minSlackValue = Int.MAX_VALUE
            for (col in 0 until n) {
                if (parentRowByCommittedCol[col] == -1 && minSlackValueByCol[col] < minSlackValue) {
                    minSlackValue = minSlackValueByCol[col]
                    minSlackRow = minSlackRowByCol[col]
                    minSlackCol = col
                }
            }
            if (minSlackValue > 0) {
                updateLabeling(minSlackValue)
            }
            parentRowByCommittedCol[minSlackCol] = minSlackRow
            if (matchRowByCol[minSlackCol] == -1) {
                var committedCol = minSlackCol
                while (committedCol != -1) {
                    val temp = matchColByRow[parentRowByCommittedCol[committedCol]]
                    match(parentRowByCommittedCol[committedCol], committedCol)
                    committedCol = temp
                }
                return
            } else {
                val row = matchRowByCol[minSlackCol]
                committedRows[numCommittedRows++] = row
                for (col in 0 until n) {
                    if (parentRowByCommittedCol[col] == -1) {
                        val slack = costs[row][col] - labelByRow[row] - labelByCol[col]
                        if (minSlackValueByCol[col] > slack) {
                            minSlackValueByCol[col] = slack
                            minSlackRowByCol[col] = row
                        }
                    }
                }
            }
        }
    }

    fun reduce() {
        for (row in 0 until n) {
            var low = costs[row][0]
            for (col in 1 until n) {
                low = min(labelByRow[row], costs[row][col])
            }
            adjustment += low
            labelByRow[row] = low
        }

        labelByCol.fill(Int.MAX_VALUE)
        for (row in 0 until n) {
            for (col in 0 until n) {
                labelByCol[col] = min(costs[row][col] - labelByRow[row], labelByCol[col])
            }
        }
        adjustment += labelByCol.sum()
    }

    inline fun greedyMatch() {
        for (row in 0 until n) {
            for (col in 0 until n) {
                if (matchColByRow[row] == -1 && matchRowByCol[col] == -1 && costs[row][col] - labelByRow[row] - labelByCol[col] == 0) {
                    match(row, col)
                }
            }
        }
    }

    inline fun fetchUnmatchedRow(): Int {
        while (++nextRowToMatch != n && matchColByRow[nextRowToMatch] != -1) continue
        return nextRowToMatch
    }


    inline fun match(row: Int, col: Int) {
        matchRowByCol[col] = row
        matchColByRow[row] = col
    }

    inline fun updateLabeling(slack: Int) {
        for(i in 0 until numCommittedRows) {
            adjustment += slack
            labelByRow[committedRows[i]] += slack
        }
        for (col in 0 until n) {
            if (parentRowByCommittedCol[col] == -1) {
                minSlackValueByCol[col] -= slack
            } else {
                adjustment -= slack
                labelByCol[col] -= slack
            }
        }
    }
}
