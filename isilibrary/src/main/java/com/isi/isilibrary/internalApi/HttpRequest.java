package com.isi.isilibrary.internalApi;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isi.isilibrary.internalApi.classes.Account;
import com.isi.isilibrary.internalApi.classes.AppActivation;
import com.isi.isilibrary.internalApi.classes.AppAndAppActivation;
import com.isi.isilibrary.internalApi.classes.BillAndFattura;
import com.isi.isilibrary.internalApi.classes.Category;
import com.isi.isilibrary.internalApi.classes.CategoryAndProduct;
import com.isi.isilibrary.internalApi.classes.Commercial;
import com.isi.isilibrary.internalApi.classes.Customer;
import com.isi.isilibrary.internalApi.classes.Fattura;
import com.isi.isilibrary.internalApi.classes.FiscalPrinter;
import com.isi.isilibrary.internalApi.classes.GYBToken;
import com.isi.isilibrary.internalApi.classes.IsiCashBillAndElements;
import com.isi.isilibrary.internalApi.classes.IsiCashDepartment;
import com.isi.isilibrary.internalApi.classes.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HttpRequest {

    private final String apiKey;

    public HttpRequest(String apiKey) {
        this.apiKey = apiKey;
    }

    public Commercial getCommercial(){

        HttpJson json = new HttpJson();

        MakeHttpPost post = new MakeHttpPost( "getCommercial", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return new Gson().fromJson(response, Commercial.class);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;

    }

    public List<Account> getAccount(){

        HttpJson json = new HttpJson();

        MakeHttpPost post = new MakeHttpPost( "getAccount", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return new Gson().fromJson(response, new TypeToken<ArrayList<Account>>(){}.getType());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;

    }

    public List<AppAndAppActivation> getApplicationActive(){

        HttpJson json = new HttpJson();

        MakeHttpPost post = new MakeHttpPost( "getApplicationActive", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return new Gson().fromJson(response, new TypeToken<ArrayList<AppAndAppActivation>>(){}.getType());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;

    }

    public boolean removeShadow(AppActivation appActivation, int position){

        HttpJson json = new HttpJson();
        json.addData("id", appActivation.id);
        json.addData("position", position);

        MakeHttpPost post = new MakeHttpPost( "removeShadow", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean addShadow(AppActivation appActivation){

        HttpJson json = new HttpJson();
        json.addData("id", appActivation.id);

        MakeHttpPost post = new MakeHttpPost( "addShadow", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public int activateService(int id, int position){

        HttpJson json = new HttpJson();
        json.addData("id", id);
        json.addData("position", position);

        MakeHttpPost post = new MakeHttpPost( "activateService", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            if(response.trim().equals("ok")) {
                return 1;
            }

            return 0;

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public boolean updateApplicationActivePriority(ArrayList<AppAndAppActivation> activations){

        HttpJson json = new HttpJson();
        json.addData("applications", new Gson().toJsonTree(activations));

        MakeHttpPost post = new MakeHttpPost( "updateApplicationActivePriority", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            Log.e("TAG", "updateApplicationActivePriority: " + response);

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean removeAppActivation(AppActivation appActivation){

        HttpJson json = new HttpJson();
        json.addData("id", appActivation.id);

        MakeHttpPost post = new MakeHttpPost( "removeAppActivation", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean setPositionInMenu(AppActivation appActivation, int position){

        HttpJson json = new HttpJson();
        json.addData("id", appActivation.id);
        json.addData("position", position);

        MakeHttpPost post = new MakeHttpPost( "setPositionInMenu", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean updateLocation(double lat, double lon){

        HttpJson json = new HttpJson();
        json.addData("lat", lat);
        json.addData("lon", lon);

        MakeHttpPost post = new MakeHttpPost( "updateLocation", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean updateNfc(Account account, String nfc){

        HttpJson json = new HttpJson();
        json.addData("id", account.id);
        json.addData("nfc", nfc);

        MakeHttpPost post = new MakeHttpPost( "updateNfc", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    //ISICASHIER

    public boolean addDepartment(IsiCashDepartment department){

        HttpJson json = new HttpJson();
        json.addData("department", new Gson().toJsonTree(department));

        MakeHttpPost post = new MakeHttpPost( "addDepartment", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean editDepartment(IsiCashDepartment department){

        HttpJson json = new HttpJson();
        json.addData("department", new Gson().toJsonTree(department));

        MakeHttpPost post = new MakeHttpPost( "editDepartment", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public List<IsiCashDepartment> getDepartment(){
        HttpJson json = new HttpJson();

        MakeHttpPost post = new MakeHttpPost( "getDepartment", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return new Gson().fromJson(response, new TypeToken<ArrayList<IsiCashDepartment>>(){}.getType());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addCategory(Category department){

        HttpJson json = new HttpJson();
        json.addData("category", new Gson().toJsonTree(department));

        MakeHttpPost post = new MakeHttpPost( "addCategory", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean editcategory(Category category){

        HttpJson json = new HttpJson();
        json.addData("category", new Gson().toJsonTree(category));

        MakeHttpPost post = new MakeHttpPost( "editCategory", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean addProduct(Product product){

        HttpJson json = new HttpJson();
        json.addData("product", new Gson().toJsonTree(product));

        MakeHttpPost post = new MakeHttpPost( "addProduct", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean editProduct(Product product){

        HttpJson json = new HttpJson();
        json.addData("product", new Gson().toJsonTree(product));

        MakeHttpPost post = new MakeHttpPost( "editProduct", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean deleteProduct(Product product){

        HttpJson json = new HttpJson();
        json.addData("product", new Gson().toJsonTree(product));

        MakeHttpPost post = new MakeHttpPost( "addProduct", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public List<CategoryAndProduct> getCategories(){
        HttpJson json = new HttpJson();

        MakeHttpPost post = new MakeHttpPost( "getCategories", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return new Gson().fromJson(response, new TypeToken<ArrayList<CategoryAndProduct>>(){}.getType());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<FiscalPrinter> getFiscalPrinter(){
        HttpJson json = new HttpJson();

        MakeHttpPost post = new MakeHttpPost( "getFiscalPrinter", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return new Gson().fromJson(response, new TypeToken<ArrayList<FiscalPrinter>>(){}.getType());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addFiscalPrinter(FiscalPrinter printer){

        HttpJson json = new HttpJson();
        json.addData("printer", new Gson().toJsonTree(printer));

        MakeHttpPost post = new MakeHttpPost( "addFiscalPrinter", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean editFiscalPrinter(FiscalPrinter printer){

        HttpJson json = new HttpJson();
        json.addData("printer", new Gson().toJsonTree(printer));

        MakeHttpPost post = new MakeHttpPost( "editFiscalPrinter", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean addGybToken(GYBToken token){

        HttpJson json = new HttpJson();
        json.addData("token", new Gson().toJsonTree(token));

        MakeHttpPost post = new MakeHttpPost( "addGybToken", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public GYBToken getGybToken(){

        HttpJson json = new HttpJson();

        MakeHttpPost post = new MakeHttpPost( "getGybToken", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return new Gson().fromJson(response, GYBToken.class);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;

    }

    public boolean addBill(IsiCashBillAndElements bill){

        HttpJson json = new HttpJson();
        json.addData("bill", new Gson().toJsonTree(bill));

        MakeHttpPost post = new MakeHttpPost( "addBill", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean addFattura(IsiCashBillAndElements bill, Fattura fattura){

        HttpJson json = new HttpJson();
        json.addData("bill", new Gson().toJsonTree(bill));
        json.addData("fattura", new Gson().toJsonTree(fattura));

        MakeHttpPost post = new MakeHttpPost( "addBill", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public List<BillAndFattura> getFatture(){
        HttpJson json = new HttpJson();

        MakeHttpPost post = new MakeHttpPost( "getFatture", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return new Gson().fromJson(response, new TypeToken<ArrayList<BillAndFattura>>(){}.getType());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteFattura(Fattura fattura){

        HttpJson json = new HttpJson();
        json.addData("fattura", new Gson().toJsonTree(fattura));

        MakeHttpPost post = new MakeHttpPost( "deleteFattura", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean updateFattura(Fattura fattura){

        HttpJson json = new HttpJson();
        json.addData("fattura", new Gson().toJsonTree(fattura));

        MakeHttpPost post = new MakeHttpPost( "updateFattura", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public List<Customer> getCustomers(){
        HttpJson json = new HttpJson();

        MakeHttpPost post = new MakeHttpPost( "getCustomers", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return new Gson().fromJson(response, new TypeToken<ArrayList<Customer>>(){}.getType());

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addCustomer(Customer customer){

        HttpJson json = new HttpJson();
        json.addData("customer", new Gson().toJsonTree(customer));

        MakeHttpPost post = new MakeHttpPost( "addCustomer", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean editCustomer(Customer customer){

        HttpJson json = new HttpJson();
        json.addData("customer", new Gson().toJsonTree(customer));

        MakeHttpPost post = new MakeHttpPost( "editCustomer", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

    public boolean deleteCustomer(Customer customer){

        HttpJson json = new HttpJson();
        json.addData("customer", new Gson().toJsonTree(customer));

        MakeHttpPost post = new MakeHttpPost( "deleteCustomer", json.getData(), apiKey);

        try {
            String response = post.execute().get();

            return response.trim().equals("ok");

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;

    }

}


