package com.isi.isilibrary.products;

public class Rates {

    public static final String A = "A";
    public static final String B = "B";
    public static final String C = "C";
    public static final String N1 = "N1";
    public static final String N2 = "N2";
    public static final String N3 = "N3";
    public static final String N4 = "N4";
    public static final String N5 = "N5";
    public static final String N6 = "N6";
    public static final String N7 = "N7";

    public static final String[] rates = {A,B,C,N1,N2,N3,N4,N5,N6,N7};

    public static int getPRINTFCode(String code){

        switch (code){

            case A:
                return 1;
            case B:
                return 2;
            case N1:
                return 8;
            case N2:
                return 9;
            case N3:
                return 10;
            case N4:
                return 0;
            case N5:
                return 11;
            case N6:
                return 12;
            default:
                return 3;


        }

    }

    public static String getRatesValor(String code){

        switch (code){

            case A:
                return "4%";
            case B:
                return "10%";
            case N1:
                return N1;
            case N2:
                return N2;
            case N3:
                return N3;
            case N4:
                return N4;
            case N5:
                return N5;
            case N6:
                return N6;
            default:
                return "22%";


        }

    }


}
