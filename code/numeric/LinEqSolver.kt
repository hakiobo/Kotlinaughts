private class LinEqSolver(val matrix: Array<DoubleArray>) {
    var status: Status? = null
        private set

    private fun swap(r1: Int, r2: Int) {
        val tmp = matrix[r1]
        matrix[r1] = matrix[r2]
        matrix[r2] = tmp

    }

    private fun multiplyRow(row: Int, mult: Double) {
        for (x in matrix[row].indices) {
            matrix[row][x] *= mult
        }
    }

    private fun divideRow(row: Int, mult: Double) {
        for (x in matrix[row].indices) {
            matrix[row][x] /= mult
        }
    }

    private fun multiplyAddRow(row: Int, row2: Int, mult: Double) {
        for (x in matrix[row].indices) {
            matrix[row2][x] += matrix[row][x] * mult
        }
    }

    private fun examineCol(col: Int, idealRow: Int): Boolean {
        var good = false
        if (matrix[idealRow][col].absoluteValue < EPSILON) {
            for (row in idealRow + 1 until matrix.size) {
                if (matrix[row][col].absoluteValue >= EPSILON) {
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
                if (matrix[row][col].absoluteValue >= EPSILON) {
                    multiplyAddRow(
                        idealRow,
                        row,
                        -matrix[row][col]
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
        private const val EPSILON = 1e-7
    }

    enum class Status {
        INCONSISTENT, SINGLE, MULTIPLE
    }
}
