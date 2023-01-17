package com.isi.isilibrary.cfbuilder

import android.content.*
import android.util.Log

object CF_Builder {
    private var Debuggable = false
    private const val TAG = "CF_BUILDER"
    private const val FemDays = 40
    private lateinit var C: Costanti
    fun init(context: Context) {
        C = Costanti(context)
    }

    fun build(personalData: PersonalData): String? {
        return try {
            var CF: String? = getSurnameCode(personalData.surname)
            CF += getNameCode(personalData.name)
            CF += getDateCode(
                personalData.dd,
                personalData.mm,
                personalData.yy,
                personalData.isGender
            )
            CF += getCodiceCatastale(personalData.birthplace)
            CF += getCheckCode(CF)
            CF
        } catch (e: Exception) {
            if (Debuggable) Log.e(TAG, e.message!!)
            null
        }
    }

    //Return the first three consonants
    private fun getSurnameCode(surname: String?): String {
        var surname = surname
        surname += "XXX" //Riempimento
        val SurnameCode = remove_vowels(surname)
        return SurnameCode.substring(0, 3)
    }

    //Return the first, the third and the fourth consonants
    private fun getNameCode(name: String?): String {
        var NameCode = remove_vowels(name)
        if (NameCode.length >= 4) NameCode =
            NameCode.substring(0, 1) + NameCode.substring(2, 4) else {
            NameCode += remove_consonants(name)
            NameCode += "XXX" //Riempimento
            NameCode = NameCode.substring(0, 3)
        }
        return NameCode
    }

    // Day, Month, Year,  gender (boolean) Male: true, Female: false
    private fun getDateCode(dd: String?, mm: String?, yy: String?, gender: Boolean): String? {
        var DateCode: String? = getYearCode(yy)
        DateCode += getMonthCode(mm)
        DateCode += getDayCode(dd, gender)
        return DateCode
    }

    // Return Day number (if Female + 40)
    private fun getDayCode(dd: String?, gender: Boolean): String {
        //Padding to avoid type errors   ex. day 4 --> day 04
        var dd = dd
        dd += "00$dd"
        dd = dd!!.substring(dd.length - 2, dd.length)

        //Add 40 if Female
        if (!gender) dd = (dd.toInt() + FemDays).toString() + ""
        return dd
    }

    // Return Month number
    private fun getMonthCode(mm: String?): String {
        return C.month_Codes[mm!!.toInt() - 1]
    }

    // Return last 2 numbers of year
    private fun getYearCode(yy: String?): String {
        //Padding to avoid type errors
        var yy = yy
        yy += "00$yy"
        yy = yy!!.substring(yy.length - 2, yy.length)
        return yy
    }

    // Return Codice Catastale of the birthplace (birthplace must be correct)
    private fun getCodiceCatastale(birthplace: String?): String? {
        return C.map_Codici_Catastali[birthplace]
    }

    private fun getCheckCode(CF: String?): String {
        var sum_odd = 0.0
        var sum_even = 0.0
        for (i in 1..15) {
            val c = CF!![i - 1].toString() + ""
            if (i % 2 == 0) sum_even += C.map_Conf_Even[c]!! else sum_odd += C.map_Conf_Odd[c]!!
        }
        var sum = sum_even + sum_odd
        sum %= 26
        return C.check_Codes[sum.toInt()]
    }

    //UTILS
    //Remove Vowels from parameter String
    private fun remove_vowels(message: String?): String {
        return message!!.replace("[^BCDFGHJKLMNPQRSTVWXYZ]".toRegex(), "")
    }

    //Remove Consonants from parameter String
    private fun remove_consonants(message: String?): String {
        return message!!.replace("[^AEIOU]".toRegex(), "")
    }

    val cityList: List<String?>?
        get() = C.cityList

    fun setDebug(debuggable: Boolean) {
        Debuggable = debuggable
    }
}