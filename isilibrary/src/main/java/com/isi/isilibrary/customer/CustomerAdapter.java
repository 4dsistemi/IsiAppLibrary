package com.isi.isilibrary.customer;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.isi.isilibrary.IsiAppActivity;
import com.isi.isilibrary.R;
import com.isi.isiapi.classes.Customer;
import com.isi.isilibrary.dialog.Dialog;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> implements Filterable {

    private final MyCustomerActivity context;
    private final List<Customer> customers;
    private final ArrayList<Customer> customersFilteres;
    private final boolean searching;

    public CustomerAdapter(@NonNull MyCustomerActivity context, @NonNull List<Customer> objects, boolean searching) {
        this.context = context;
        this.customers = objects;
        this.customersFilteres = new ArrayList<>(customers);
        this.searching = searching;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_view_customer, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Customer c = customersFilteres.get(position);

        holder.nameSurnameCustomer.setText(String.format("%s %s", c.name, c.surname));

        if(!StringUtils.isAnyEmpty(c.address, c.country, c.city, c.zip, c.province)){
            holder.addressCustomer.setText(String.format("Indirizzo: %s %s %s %s %s", c.address, c.city, c.zip, c.country, c.province));
        }else{
            holder.addressCustomer.setText("");
        }

        if(c.fiscal != null){
            holder.fiscalCustomer.setText(String.format("Codice fiscale: %s", c.fiscal));
        }else{
            holder.addressCustomer.setText("");
        }

        if(c.birthplace != null){
            holder.birthplace.setText(String.format("Nato a: %s", c.birthplace));
        }else{
            holder.birthplace.setText("");
        }

        if(c.zip != null){
            holder.zip.setText(String.format("CAP: %s", c.zip));
        }else{
            holder.zip.setText("");
        }

        if(c.birthday != null){
            holder.birthday.setText(String.format("Data di nascita: %s", c.birthday));
        }else{
            holder.birthday.setText("");
        }

        holder.modify.setOnClickListener(v -> {
            Intent i = new Intent(context, AddCustomer.class);
            i.putExtra("customer", new Gson().toJson(c));
            context.startActivity(i);
        });

        holder.delete.setOnClickListener(v ->

                new Dialog(context).yesNoDialog("Cancellazione cliente", "Sei sicuro di voler eliminare " + c.name + " " + c.surname + "?",
                        (dialogInterface, i) -> {
                            dialogInterface.dismiss();

                            AlertDialog loader = new Dialog(context).showLoadingDialog("Elimino cliente...");

                            new Thread(() -> {

                                boolean response = IsiAppActivity.isiCashierRequest.deleteCustomer(c);

                                context.runOnUiThread(() -> {
                                    loader.dismiss();

                                    if(response){
                                        context.updateUI();
                                    }else{
                                        new Dialog(context).showErrorConnectionDialog(false);
                                    }
                                });

                            }).start();
                        }, null));

        if(searching){
            holder.cardView.setOnClickListener(v -> {

                Intent i = new Intent();
                i.putExtra("customer", new Gson().toJson(c));
                context.setResult(Activity.RESULT_OK, i);
                context.finish();

            });
        }

    }

    @Override
    public int getItemCount() {
        return customersFilteres.size();
    }

    @Override
    public Filter getFilter() {
        return MuaFilter;
    }

    private final Filter MuaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Customer> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(customers);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Customer item : customers) {
                    if (item.name.toLowerCase().contains(filterPattern.toLowerCase()) || item.surname.toLowerCase().contains(filterPattern.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            customersFilteres.clear();
            customersFilteres.addAll((ArrayList<Customer>) results.values);
            notifyDataSetChanged();

        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameSurnameCustomer;
        TextView addressCustomer;
        TextView fiscalCustomer;
        TextView birthplace;
        TextView zip;
        TextView birthday;
        Button modify;
        Button delete;
        MaterialCardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameSurnameCustomer = itemView.findViewById(R.id.nameProductList);
            addressCustomer = itemView.findViewById(R.id.addressCustomerList);
            fiscalCustomer = itemView.findViewById(R.id.fiscalCustomerList);
            birthplace = itemView.findViewById(R.id.birthplaceCustomerDisplay);
            zip = itemView.findViewById(R.id.zipCustomerDisplay);
            birthday = itemView.findViewById(R.id.birthdayCustomerDisplay);
            modify = itemView.findViewById(R.id.modifyCustomerButton);
            delete = itemView.findViewById(R.id.deleteCustomerButton);
            cardView = itemView.findViewById(R.id.cardViewLayout);
        }
    }
}
