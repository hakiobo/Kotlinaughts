/**
 * Add all the words you need with the add function
 * then call build failures once you're done setting up the dictionary
 */
private class AhoCorasick(maxNodes: Int) {
    val trie = Array(maxNodes) { IntArray(ALPHA_SIZE) { -1 } }
    val wordId = IntArray(maxNodes) { -1 }
    var numNodes = 1
    var numWords = 0
    val fail = IntArray(maxNodes)
    val suffixFail = IntArray(maxNodes) { -1 }

    fun add(s: String) {
        var curNode = 0
        var curIdx = 0
        while (curIdx != s.length) {
            val c = s[curIdx] - BASE_CHAR
            if (trie[curNode][c] == -1) {
                trie[curNode][c] = numNodes++
            }
            curNode = trie[curNode][c]
            curIdx++
        }
        if (wordId[curNode] == -1) {
            wordId[curNode] = numWords++
        }
    }

    fun buildFailures() {
        val queue = IntArray(numNodes)
        var inIdx = 0
        var outidx = 0

        for (c in 0 until ALPHA_SIZE) {
            if (trie[0][c] == -1) {
                trie[0][c] = 0
            } else {
                queue[inIdx++] = trie[0][c]
            }
        }
        while (outidx < inIdx) {
            val curState = queue[outidx++]
            for (c in 0 until ALPHA_SIZE) {
                val failure = trie[fail[curState]][c]
                if (trie[curState][c] != -1) {
                    fail[trie[curState][c]] = failure

                    if (wordId[failure] != -1) {
                        suffixFail[trie[curState][c]] = failure
                    } else {
                        suffixFail[trie[curState][c]] = suffixFail[failure]
                    }
                    queue[inIdx++] = trie[curState][c]
                } else {
                    trie[curState][c] = failure
                }
            }
        }
    }

    fun nextState(curState: Int, ch: Char): Int {
        return trie[curState][ch - BASE_CHAR]
    }

    companion object {
        const val BASE_CHAR = 'a'
        const val ALPHA_SIZE = 26
    }
}
