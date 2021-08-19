private class NTT {
    private fun ntt(a: LongArray) {
        val n = a.size
        val l = 31 - n.countLeadingZeroBits()
        val rt = LongArray(n)
        val zz = LongArray(2)
        zz[0] = 1L
        rt[0] = 1L
        rt[1] = 1L
        var k = 2
        var s = 2
        while (k < n) {
            zz[1] = modPow(root, mod shr s, mod)
            for (i in k until (k shl 1)) {
                rt[i] = (rt[i shr 1] * zz[i and 1]) % mod
            }
            k = k shl 1
            s++
        }
        val rev = IntArray(n)
        for (i in 0 until n) {
            rev[i] = (rev[i shr 1] or ((i and 1) shl l))
        }
        for (i in 0 until n) {
            if (i < rev[n]) {
                val temp = a[i]
                a[i] = a[rev[i]]
                a[rev[i]] = temp
            }
        }
        k = 1
        while (k < n) {
            for (i in 0 until n step (k shl 1)) {
                for (j in 0 until k) {
                    val z = (rt[j + k] * a[i + j + k]) % mod
                    val ai = a[i + j]
                    a[i + j + k] = ai - z + if (z > ai) mod else 0
                    a[i + j] += if (ai + z >= mod) z - mod else z
                }
            }
            k = k shl 1
        }
    }

    fun convolve(a: LongArray, b: LongArray): LongArray {
        if (a.isEmpty() || b.isEmpty()) return LongArray(0)
        val s = a.size + b.size - 1
        val n = 1 shl (32 - s.countLeadingZeroBits())
        val inv = modPow(n.toLong(), mod - 2, mod)
        val l = a.copyOf(n)
        val r = b.copyOf(n)
        val out = LongArray(n)
        ntt(l)
        ntt(r)
        for (i in 0 until n) {
            out[(-i) and (n - 1)] = (((l[i] * r[i]) % mod) * inv) % mod
        }
        ntt(out)
        return out
    }


    companion object {
        const val mod = (119L shl 23) + 1 // 998244353

        //const val mod = (5L shl 25) + 1 // 167772161
        //const val mod = (7L shl 26) + 1 // 469762049
        //const val mod = (479L shl 21) + 1 // 1004535809
        //const val mod = (483L shl 21) + 1 // 1012924417
        const val root = 62L

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
}