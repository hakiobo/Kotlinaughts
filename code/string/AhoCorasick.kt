import java.util.*

/**
 * Add all the words you need with the add function
 * then call build failures once you're done setting up the dictionary
 *
 * You'll likely need to mess with countMatches to make it do exactly what you need for your current problem
 */
private class AhoCorasick(n: Int) {
    val trie = Array(n) { IntArray(26) { -1 } }
    val wordId = IntArray(n) { -1 }
    var numNodes = 1
    val wordToNode = mutableListOf<Int>()
    val wordCount = mutableListOf<Int>()
    val completedWords = Array(n) { TreeSet<Int>() }
    val fail = IntArray(n) { -1 }

    fun addAllAndBuild(dict: List<String>) {
        for (s in dict) {
            add(s)
        }
        buildFailures()
    }

    private fun add(s: String) {
        var curNode = 0
        var curDepth = 0
        var curIdx = 0
        while (curIdx != s.length) {
            val c = s[curIdx] - 'a'
            if (trie[curNode][c] == -1) {
                trie[curNode][c] = numNodes++
            }
            curNode = trie[curNode][c]
            curDepth++
            curIdx++
        }
        if (wordId[curNode] == -1) {
            wordId[curNode] = wordToNode.size
            wordCount += 0
            wordToNode += curNode
            completedWords[curNode].add(wordId[curNode])
        }
    }

    private fun buildFailures() {
        val queue = ArrayDeque<Int>()

        for (c in 0 until 26) {
            if (trie[0][c] == -1) {
                trie[0][c] = 0
            } else {
                fail[trie[0][c]] = 0
                queue += trie[0][c]
            }
        }
        while (queue.isNotEmpty()) {
            val curState = queue.poll()
            for (c in 0 until 26) {
                if (trie[curState][c] != -1) {
                    var failure = fail[curState]
                    while (trie[failure][c] == -1) {
                        failure = fail[failure]
                    }
                    failure = trie[failure][c]
                    fail[trie[curState][c]] = failure


                    //stuff
                    completedWords[trie[curState][c]].addAll(completedWords[failure])


                    queue += trie[curState][c]
                }
            }
        }
    }

    private fun nextState(curState: Int, ch: Char): Int {
        val c = ch - 'a'
        var ret = curState
        while (trie[ret][c] == -1) {
            ret = fail[ret]
        }
        return trie[ret][c]
    }

    fun countMatches(s: String): Int {
        var curState = 0
        var ct = 0
        for (c in s) {
            curState = nextState(curState, c)
            ct += completedWords[curState].size
        }
        return ct
    }
}