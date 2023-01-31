package com.isi.isilibrary.products.ingredients

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.isi.isiapi.classes.Ingredients
import com.isi.isiapi.classes.isimaga.ProductForniture
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.dialog.Dialog
import com.isi.isilibrary.dialog.MaterialTextAndListener
import com.isi.isilibrary.dialog.RapidEditText
import java.util.*
import kotlin.collections.ArrayList
import kotlin.streams.toList

class IngredientsRecycler(private val context: Context, private val product : List<ProductForniture>, private val ingredients: ArrayList<Ingredients>) : RecyclerView.Adapter<IngredientsRecycler.ViewHolder>(){

    private var ingredientsFilter : List<ProductForniture> = ArrayList()

    init {
        ingredientsFilter = ArrayList(product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.add_ingredients_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredientsFilter.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = ingredientsFilter[position]

        holder.name.text = product.name

        holder.addRemove.setOnClickListener {
            if (holder.addRemove.text == "+") {
                val quantity = RapidEditText(context)
                quantity.setEditTextNumber(decimal = true, signed = false)
                quantity.hint = "Quantità..."
                Dialog(context).showNormalDialogType(null,
                    "Aggiungi quantità in " + IsiAppActivity.isiCashierRequest!!.transformIsimagaUnity(
                        product.unity_id
                    ),
                    null,
                    MaterialTextAndListener("Ok") { dialogInterface: DialogInterface, _: Int ->
                        try {
                            val ingredient = Ingredients(
                                product.id,
                                0,
                                quantity.textOrEmpty.toFloat()
                            )
                            ingredients.add(ingredient)
                            holder.addRemove.text = "-"
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Formato non corretto",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        dialogInterface.dismiss()
                    },
                    null,
                    quantity
                )
            } else {
                for (i in ingredients) {
                    if (i.product_forniture_id == product.id) {
                        ingredients.remove(i)
                        break
                    }
                }
                holder.addRemove.text = "+"
            }
        }

    }


    fun search(search: String){

        ingredientsFilter = product.stream().filter { ing -> ing.name.lowercase(Locale.getDefault())
            .contains(search) }.toList()

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var addRemove: Button

        init {
            name = itemView.findViewById(R.id.nameElementText)
            addRemove = itemView.findViewById(R.id.buttonPlusLayout)
        }
    }
}