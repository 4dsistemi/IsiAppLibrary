package com.isi.isilibrary.customer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.isi.isiapi.general.classes.Customer;
import com.isi.isilibrary.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> implements Filterable {

    private final Activity context;
    private final List<Customer> customers;
    private final ArrayList<Customer> customersFilteres;

    public CustomerAdapter(@NonNull Activity context, @NonNull List<Customer> objects) {
        this.context = context;
        this.customers = objects;
        this.customersFilteres = new ArrayList<>(customers);
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

        holder.nameSurnameCustomer.setText(String.format("%s %s", c.getName(), c.getSurname()));

        if(!StringUtils.isAnyEmpty(c.getAddress(), c.getCountry(), c.getCity(), c.getZip(), c.getProvince())){
            holder.addressCustomer.setText(String.format("Indirizzo: %s %s %s %s %s", c.getAddress(), c.getCity(), c.getZip(), c.getCountry(), c.getProvince()));
        }

        if(c.getFiscal() != null){
            holder.fiscalCustomer.setText(String.format("Codice fiscale: %s", c.getFiscal()));
        }

        if(c.getBirthplace() != null){
            holder.birthplace.setText(String.format("Nato a: %s", c.getBirthplace()));
        }

        if(c.getZip() != null){
            holder.zip.setText(String.format("CAP: %s", c.getZip()));
        }

        if(c.getBirthday() != null){
            holder.birthday.setText(String.format("Data di nascita: %s", c.getBirthday()));
        }

        holder.modify.setOnClickListener(v -> {
            Intent i = new Intent(context, AddCustomer.class);
            i.putExtra("customer", new Gson().toJson(c));
            context.startActivity(i);
        });

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
                    if (item.getName().toLowerCase().contains(filterPattern.toLowerCase()) || item.getSurname().toLowerCase().contains(filterPattern.toLowerCase())) {
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

        public ViewHolder(View itemView) {
            super(itemView);
            nameSurnameCustomer = itemView.findViewById(R.id.nameProductList);
            addressCustomer = itemView.findViewById(R.id.addressCustomerList);
            fiscalCustomer = itemView.findViewById(R.id.fiscalCustomerList);
            birthplace = itemView.findViewById(R.id.birthplaceCustomerDisplay);
            zip = itemView.findViewById(R.id.zipCustomerDisplay);
            birthday = itemView.findViewById(R.id.birthdayCustomerDisplay);
            modify = itemView.findViewById(R.id.modifyCustomerButton);
        }
    }
}
