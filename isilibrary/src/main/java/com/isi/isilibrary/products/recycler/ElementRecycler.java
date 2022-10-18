package com.isi.isilibrary.products.recycler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.isi.isiapi.classes.Product;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isilibrary.products.AddManageElementActivity;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.androidhive.fontawesome.FontTextView;

public class ElementRecycler extends RecyclerView.Adapter<ElementRecycler.ViewHolder> {

    private final Context context;
    private final List<Product> products;

    public ElementRecycler(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.element_panel, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product p = products.get(position);


        holder.name.setText(String.format("%s", p.name));
        holder.name.setLines(2);
        holder.name.setMaxLines(2);
        holder.price.setText(String.format(Locale.getDefault(), "%.2f €\n%.2f €", p.price, p.price_banco));

        holder.active.setOnClickListener(view -> {

            if(holder.active.isChecked()){
                p.active = 1;
            }else{
                p.active = 0;
            }

            new Thread(() -> {
                if(!IsiAppActivity.isiCashierRequest.editProduct(p, null)){
                    ((Activity)context).runOnUiThread(() -> new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Errore di connessione")
                            .show());

                }
            }).start();
        });

        holder.active.setChecked(p.active == 1);

        holder.orderGuest.setChecked(p.guest == 1);

        holder.orderGuest.setOnClickListener(view -> {

            if(holder.orderGuest.isChecked()){
                p.guest = 1;
            }else{
                p.guest = 0;
            }

            new Thread(() -> {
                if(!IsiAppActivity.isiCashierRequest.editProduct(p, null)){
                    ((Activity)context).runOnUiThread(() -> new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Errore di connessione")
                            .show());

                }
            }).start();

        });

        holder.modify.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddManageElementActivity.class);
            intent.putExtra("product", new Gson().toJson(p));
            context.startActivity(intent);
        });

        holder.priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int backPriority = p.priority_prod;

                if(backPriority != position){
                    p.priority_prod = position;

                    IsiAppActivity.isiCashierRequest.editProduct(p, null);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner = holder.priority;

        int categoryProduct = p.category_id;
        List<Product> allProductsInCat = products.stream().filter(product -> product.category_id == categoryProduct).collect(Collectors.toList());
        int counting = allProductsInCat.size();

        Integer[] arrayCount = new Integer[counting + 1];

        for (int i = 0; i <= counting; i++) {
            arrayCount[i] = i;
        }

        ArrayAdapter<Integer> aa = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,arrayCount);
        spinner.setAdapter(aa);

        spinner.setSelection(p.priority_prod);

    }

    @Override
    public int getItemCount() {
        return (products != null) ? products.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        FontTextView modify;
        Spinner priority;
        CheckBox active;
        CheckBox orderGuest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.costoCopertoPrice);
            modify = itemView.findViewById(R.id.modifyCostoCopertoPrice);
            priority = itemView.findViewById(R.id.priority_spinner_element_cell);
            active = itemView.findViewById(R.id.element_active_check);
            orderGuest = itemView.findViewById(R.id.element_guest_active);

        }
    }
}
