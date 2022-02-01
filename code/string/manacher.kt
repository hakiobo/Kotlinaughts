private fun manacher(si: String): IntArray {
    val s = CharArray((si.length shl 1) - 1) { idx ->
        if (idx and 1 == 0) {
            si[idx shr 1]
        } else {
            '#'
        }
    }
    var center = 0
    var radius = 0
    val ans = IntArray(s.size)
    while (center < s.size) {
        while (center - radius - 1 >= 0 && center + radius + 1 < s.size && s[center - radius - 1] == s[center + radius + 1]) {
            radius++
        }
        ans[center] = radius


        val oldCenter = center
        val oldRadius = radius
        center++
        radius = 0
        while (center <= oldCenter + oldRadius) {
            val mirroredCenter = (oldCenter shl 1) - center
            val maxMirroredRadius = oldCenter + oldRadius - center
            if (ans[mirroredCenter] < maxMirroredRadius) {
                ans[center] = ans[mirroredCenter]
                center++
            } else if (ans[mirroredCenter] > maxMirroredRadius) {
                ans[center] = maxMirroredRadius
                center++
            } else {
                radius = maxMirroredRadius
                break
            }
        }
    }
    return ans
}
