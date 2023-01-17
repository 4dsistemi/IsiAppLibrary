package com.isi.isilibrary.cfbuilder

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.isi.isilibrary.R
import java.io.InputStreamReader

class Costanti(context: Context) {

    lateinit var map_Conf_Odd: Map<String?, Double?>
        private set
    lateinit var map_Conf_Even: Map<String?, Double?>
        private set
    lateinit var map_Codici_Catastali: Map<String?, String?>
        private set
    var cityList: List<String?>? = null

    //CONSTRUCTOR
    init {
        init_Map_Conf_Even(context)
        init_Map_Conf_Odd(context)
        init_Map_Codici_Catastali(context)
        cityList = ArrayList(map_Codici_Catastali!!.keys)
    }

    //INIT EVEN AND ODD CODES
    private fun init_Map_Conf_Odd(context: Context) {
        val `is` = context.resources.openRawResource(R.raw.odd_codes)
        val reader = JsonReader(InputStreamReader(`is`))
        map_Conf_Odd = Gson().fromJson(reader, HashMap::class.java)
    }

    private fun init_Map_Conf_Even(context: Context) {
        val `is` = context.resources.openRawResource(R.raw.even_codes)
        val reader = JsonReader(InputStreamReader(`is`))
        map_Conf_Even = Gson().fromJson(reader, HashMap::class.java)
    }

    private fun init_Map_Codici_Catastali(context: Context) {
        val `is` = context.resources.openRawResource(R.raw.codici)
        val reader = JsonReader(InputStreamReader(`is`))
        map_Codici_Catastali = Gson().fromJson(reader, HashMap::class.java)
    }

    val month_Codes = arrayOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "H",
        "L",
        "M",
        "P",
        "R",
        "S",
        "T"
    )
    val sameName_Codes = arrayOf(
        "L",
        "M",
        "N",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V"
    )
    val check_Codes = arrayOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
        "H",
        "I",
        "J",
        "K",
        "L",
        "M",
        "N",
        "O",
        "P",
        "Q",
        "R",
        "S",
        "T",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z"
    )
}