private class ModLinEqSolver(val matrix: Array<IntArray>, val p: Int) {
    val inv = IntArray(p) { num ->
        modPow(num.toLong(), (p - 2).toLong(), p.toLong()).toInt()
    }
    var status: Status? = null
        private set

    private fun swap(r1: Int, r2: Int) {
        val tmp = matrix[r1]
        matrix[r1] = matrix[r2]
        matrix[r2] = tmp
    }

    private fun multiplyRow(row: Int, mult: Int) {
        for (x in matrix[row].indices) {
            matrix[row][x] *= mult
            matrix[row][x] %= p
        }
    }

    private fun divideRow(row: Int, div: Int) {
        multiplyRow(row, inv[div])
    }

    private fun multiplyAddRow(row: Int, row2: Int, mult: Int) {
        for (x in matrix[row].indices) {
            matrix[row2][x] += matrix[row][x] * mult
            matrix[row2][x] %= p
        }
    }

    private fun examineCol(col: Int, idealRow: Int): Boolean {
        var good = false
        if (matrix[idealRow][col] == 0) {
            for (row in idealRow + 1 until matrix.size) {
                if (matrix[row][col] != 0) {
                    good = true
                    swap(row, idealRow)
                    break
                }
            }
        } else {
            good = true
        }
        if (good) {
            divideRow(
                idealRow,
                matrix[idealRow][col]
            )
            for (row in matrix.indices) {
                if (row == idealRow) continue
                if (matrix[row][col] != 0) {
                    multiplyAddRow(
                        idealRow,
                        row,
                        p - matrix[row][col]
                    )
                }
            }
        }
        return good
    }

    fun solve() {
        var idealRow = 0
        var col = 0
        while (idealRow < matrix.size && col < matrix[0].lastIndex) {
            if (examineCol(col++, idealRow)) idealRow++
        }
        status = if (col != matrix[0].lastIndex) {
            Status.MULTIPLE
        } else if (idealRow != matrix.size && examineCol(col, idealRow)) {
            Status.INCONSISTENT
        } else if (idealRow == col) {
            Status.SINGLE
        } else {
            Status.MULTIPLE
        }
    }

    companion object {
        private fun modPow(n: Long, k: Long, m: Long): Long {
            if (k == 0L) {
                return 1L
            }
            var half = modPow(n, k shr 1, m)
            half *= half
            if (k and 1L == 1L) {
                half %= m
                half *= n
            }
            return half % m
        }
    }

    enum class Status {
        INCONSISTENT, SINGLE, MULTIPLE
    }
}
