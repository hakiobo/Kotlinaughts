// https://open.kattis.com/problems/cordonbleu
// https://open.kattis.com/problems/toursdesalesforce
// Min Cost Match Matching
private class Hungarian(val costs: Array<IntArray>) { // assuming a square matrix of costs
    val n = costs.size
    var adjustment = 0
    val adjustmentByRow = IntArray(n)
    val adjustmentByCol = IntArray(n)

    val minSlackRowByCol = IntArray(n)
    val minSlackValueByCol = IntArray(n)
    val committedRows = IntArray(n)
    var numCommittedRows = 0
    val parentRowByCommittedCol = IntArray(n)
    val matchColByRow = IntArray(n) { -1 }
    val matchRowByCol = IntArray(n) { -1 }

    fun findBestMatching(): Int { // returns the final cost, can get the arrangements from matchRowByCol an matchColByRow

        // get some values down to 0 to start
        for (row in 0 until n) {
            var low = costs[row][0]
            for (col in 1 until n) {
                low = min(adjustmentByRow[row], costs[row][col])
            }
            adjustment += low
            adjustmentByRow[row] = low
        }
        adjustmentByCol.fill(Int.MAX_VALUE)
        for (row in 0 until n) {
            for (col in 0 until n) {
                adjustmentByCol[col] = min(costs[row][col] - adjustmentByRow[row], adjustmentByCol[col])
            }
        }
        adjustment += adjustmentByCol.sum()

        // greedily set up some matches
        for (row in 0 until n) {
            for (col in 0 until n) {
                if (matchColByRow[row] == -1 && matchRowByCol[col] == -1 && costs[row][col] - adjustmentByRow[row] - adjustmentByCol[col] == 0) {
                    matchRowByCol[col] = row
                    matchColByRow[row] = col
                }
            }
        }

        for (row in 0 until n) {
            if (matchColByRow[row] == -1) {
                // set some stuff up
                parentRowByCommittedCol.fill(-1)
                committedRows[0] = row
                numCommittedRows = 1
                minSlackRowByCol.fill(row)
                for (col in 0 until n) {
                    minSlackValueByCol[col] = costs[row][col] - adjustmentByRow[row] - adjustmentByCol[col]
                }

                executePhase()
            }
        }
        return adjustment
    }


    fun executePhase() {
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
                adjustment += minSlackValue
                for (i in 0 until numCommittedRows) {
                    adjustmentByRow[committedRows[i]] += minSlackValue
                }
                for (col in 0 until n) {
                    if (parentRowByCommittedCol[col] == -1) {
                        minSlackValueByCol[col] -= minSlackValue
                    } else {
                        adjustmentByCol[col] -= minSlackValue
                    }
                }
            }

            parentRowByCommittedCol[minSlackCol] = minSlackRow
            if (matchRowByCol[minSlackCol] == -1) {
                var committedCol = minSlackCol
                while (committedCol != -1) {
                    val row = parentRowByCommittedCol[committedCol]

                    val temp = matchColByRow[row]
                    matchRowByCol[committedCol] = row
                    matchColByRow[row] = committedCol
                    committedCol = temp
                }
                return
            } else {
                val row = matchRowByCol[minSlackCol]
                committedRows[numCommittedRows++] = row
                for (col in 0 until n) {
                    if (parentRowByCommittedCol[col] == -1) {
                        val slack = costs[row][col] - adjustmentByRow[row] - adjustmentByCol[col]
                        if (minSlackValueByCol[col] > slack) {
                            minSlackValueByCol[col] = slack
                            minSlackRowByCol[col] = row
                        }
                    }
                }
            }
        }
    }
}
