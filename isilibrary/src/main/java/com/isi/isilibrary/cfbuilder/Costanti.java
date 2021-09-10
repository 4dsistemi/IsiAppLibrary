package com.isi.isilibrary.cfbuilder;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.isi.isilibrary.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Costanti {

    private static String[] Month_Codes = {
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
    };

    private static String[] SameName_Codes = {
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
    };

    private static String[] Check_Codes = {
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
    };

    private Map<String, Double> Map_Conf_Odd;
    private Map<String, Double> Map_Conf_Even;
    private Map<String, String> Map_Codici_Catastali;
    private List<String> CityList;

    //CONSTRUCTOR

    public Costanti(Context context) {
        init_Map_Conf_Even(context);
        init_Map_Conf_Odd(context);
        init_Map_Codici_Catastali(context);
        setCityList(new ArrayList<String>(Map_Codici_Catastali.keySet()));
    }

    //INIT EVEN AND ODD CODES

    private void init_Map_Conf_Odd(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.odd_codes);
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        Map_Conf_Odd = new Gson().fromJson(reader, HashMap.class);
    }

    private void init_Map_Conf_Even(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.even_codes);
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        Map_Conf_Even = new Gson().fromJson(reader, HashMap.class);
    }

    private void init_Map_Codici_Catastali(Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.codici);
        JsonReader reader = new JsonReader(new InputStreamReader(is));
        Map_Codici_Catastali = new Gson().fromJson(reader, HashMap.class);
    }


    //GET METHODS

    public String[] getMonth_Codes() {
        return Month_Codes;
    }

    public String[] getSameName_Codes() {
        return SameName_Codes;
    }

    public String[] getCheck_Codes() {
        return Check_Codes;
    }

    public Map<String, Double> getMap_Conf_Odd() {
        return Map_Conf_Odd;
    }

    public Map<String, Double> getMap_Conf_Even() {
        return Map_Conf_Even;
    }

    public Map<String, String> getMap_Codici_Catastali() {
        return Map_Codici_Catastali;
    }

    public List<String> getCityList() {
        return CityList;
    }

    public void setCityList(List<String> cityList) {
        CityList = cityList;
    }
}
