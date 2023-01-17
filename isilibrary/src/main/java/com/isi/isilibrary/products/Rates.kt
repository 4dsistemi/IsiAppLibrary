package com.isi.isilibrary.products

object Rates {
    const val A = "A"
    const val B = "B"
    const val C = "C"
    const val N1 = "N1"
    const val N2 = "N2"
    const val N3 = "N3"
    const val N4 = "N4"
    const val N5 = "N5"
    const val N6 = "N6"
    const val N7 = "N7"
    val rates = arrayOf(A, B, C, N1, N2, N3, N4, N5, N6, N7)
    fun getPRINTFCode(code: String?): Int {
        return when (code) {
            A -> 1
            B -> 2
            N1 -> 8
            N2 -> 9
            N3 -> 10
            N4 -> 0
            N5 -> 11
            N6 -> 12
            else -> 3
        }
    }

    fun getRatesValor(code: String?): String {
        return when (code) {
            A -> "4%"
            B -> "10%"
            N1 -> N1
            N2 -> N2
            N3 -> N3
            N4 -> N4
            N5 -> N5
            N6 -> N6
            else -> "22%"
        }
    }
}