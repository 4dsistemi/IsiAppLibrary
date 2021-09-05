package com.isi.isilibrary.products;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.isi.isiapi.general.classes.Product;
import com.isi.isilibrary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> implements Filterable {

    private final Activity context;
    private final List<Product> products;
    private final ArrayList<Product> productsfilter;

    public ProductsAdapter(@NonNull Activity context, @NonNull List<Product> objects) {
        this.context = context;
        this.products = objects;
        this.productsfilter = new ArrayList<>(products);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_products, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product c = productsfilter.get(position);

        holder.nameProduct.setText(String.format("%s", c.name));

        holder.priceProduct.setText(String.format(Locale.getDefault(), "Prezzo: %.2f", c.price));
        holder.barcode.setText(c.barcode);

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return productsfilter.size();
    }

    @Override
    public Filter getFilter() {
        return MuaFilter;
    }

    private final Filter MuaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(products);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Product item : products) {
                    if (item.name.toLowerCase().contains(filterPattern.toLowerCase())) {
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
            productsfilter.clear();
            productsfilter.addAll((ArrayList<Product>) results.values);
            notifyDataSetChanged();

        }
    };



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameProduct;
        TextView priceProduct;
        TextView barcode;
        Button modify;

        public ViewHolder(View itemView) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.nameProductList);
            priceProduct = itemView.findViewById(R.id.priceProductList);
            barcode = itemView.findViewById(R.id.barcodeValueProduct);
            modify = itemView.findViewById(R.id.modifyProduct);
        }
    }
}
