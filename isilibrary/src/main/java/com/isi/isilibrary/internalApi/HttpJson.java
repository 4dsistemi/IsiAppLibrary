package com.isi.isilibrary.internalApi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class HttpJson {

    private final JsonObject json;

    public HttpJson() {
        this.json = new JsonObject();
    }

    public void addData(String name, Object variable){

        if(variable == null){
            json.add(name, null);
        }else{
            if(variable instanceof JsonElement){
                json.add(name, (JsonElement)variable);
            }else{
                json.addProperty(name, variable.toString());
            }
        }


    }

    public JsonObject getData(){
        return json;
    }
}