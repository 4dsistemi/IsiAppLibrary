package com.isi.isilibrary.products.recycler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.isi.isiapi.classes.Product
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.dialog.Dialog
import com.isi.isilibrary.products.AddManageElementActivity
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

class ElementRecycler(private val context: Context, private val products: List<Product>) :
    RecyclerView.Adapter<ElementRecycler.ViewHolder>() {

    private var productsSeleccted: List<Product> = ArrayList()

    init {
        productsSeleccted = ArrayList(products)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.element_panel, parent, false)
        return ViewHolder(view)
    }

    fun search(category: Int?, search: String){
        productsSeleccted = if (category == null) {
            products.filter { it.name.lowercase().contains(search.lowercase() )}
        } else {
            products.filter {
                it.name.lowercase().contains(
                    search.lowercase()
                ) && it.category_id == category
            }.toList()
        }

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = productsSeleccted[position]
        holder.name.text = String.format("%s", p.name)
        holder.name.setLines(2)
        holder.name.maxLines = 2
        holder.price.text =
            String.format(Locale.getDefault(), "%.2f €\n%.2f €", p.price, p.price_banco)
        holder.active.setOnClickListener {
            if (holder.active.isChecked) {
                p.active = 1
            } else {
                p.active = 0
            }
            Thread {
                if (!IsiAppActivity.httpRequest!!.editProduct(p, null)) {
                    (context as Activity).runOnUiThread {
                        Dialog(
                            context
                        ).showErrorConnectionDialog(false)
                    }
                }
            }.start()
        }
        holder.active.isChecked = p.active == 1
        holder.orderGuest.isChecked = p.guest == 1
        holder.orderGuest.setOnClickListener {
            if (holder.orderGuest.isChecked) {
                p.guest = 1
            } else {
                p.guest = 0
            }
            Thread {
                if (!IsiAppActivity.httpRequest!!.editProduct(p, null)) {
                    (context as Activity).runOnUiThread {
                        Dialog(
                            context
                        ).showErrorConnectionDialog(false)
                    }
                }
            }.start()
        }
        holder.modify.setOnClickListener {
            val intent = Intent(context, AddManageElementActivity::class.java)
            intent.putExtra("product", Gson().toJson(p))
            context.startActivity(intent)
        }
        holder.priority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val backPriority = p.priority_prod
                if (backPriority != position) {
                    p.priority_prod = position
                    IsiAppActivity.httpRequest!!.editProduct(p, null)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val spinner = holder.priority
        val categoryProduct = p.category_id
        val allProductsInCat =
            products.stream().filter { product: Product -> product.category_id == categoryProduct }
                .collect(Collectors.toList())
        val counting = allProductsInCat.size
        val arrayCount = arrayOfNulls<Int>(counting + 1)
        for (i in 0..counting) {
            arrayCount[i] = i
        }
        val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, arrayCount)
        spinner.adapter = aa
        spinner.setSelection(p.priority_prod)
    }

    override fun getItemCount(): Int {
        return productsSeleccted.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var price: TextView
        var modify: Button
        var priority: Spinner
        var active: CheckBox
        var orderGuest: CheckBox

        init {
            name = itemView.findViewById(R.id.name)
            price = itemView.findViewById(R.id.costoCopertoPrice)
            modify = itemView.findViewById(R.id.modifyCostoCopertoPrice)
            priority = itemView.findViewById(R.id.priority_spinner_element_cell)
            active = itemView.findViewById(R.id.element_active_check)
            orderGuest = itemView.findViewById(R.id.element_guest_active)
        }
    }
}