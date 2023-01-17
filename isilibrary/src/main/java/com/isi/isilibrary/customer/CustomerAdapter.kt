package com.isi.isilibrary.customer

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.isi.isiapi.classes.Customer
import com.isi.isilibrary.IsiAppActivity
import com.isi.isilibrary.R
import com.isi.isilibrary.dialog.Dialog
import org.apache.commons.lang3.StringUtils
import java.util.*

class CustomerAdapter(
    private val context: MyCustomerActivity,
    private val customers: List<Customer>,
    searching: Boolean
) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>(), Filterable {


    private val customersFilteres: ArrayList<Customer> = ArrayList(customers)
    private val searching: Boolean

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_view_customer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val c = customersFilteres[position]
        holder.nameSurnameCustomer.text = String.format("%s %s", c.name, c.surname)
        if (!StringUtils.isAnyEmpty(c.address, c.country, c.city, c.zip, c.province)) {
            holder.addressCustomer.text = String.format(
                "Indirizzo: %s %s %s %s %s",
                c.address,
                c.city,
                c.zip,
                c.country,
                c.province
            )
        } else {
            holder.addressCustomer.text = ""
        }
        if (c.fiscal != null) {
            holder.fiscalCustomer.text = String.format("Codice fiscale: %s", c.fiscal)
        } else {
            holder.addressCustomer.text = ""
        }
        if (c.birthplace != null) {
            holder.birthplace.text = String.format("Nato a: %s", c.birthplace)
        } else {
            holder.birthplace.text = ""
        }
        if (c.zip != null) {
            holder.zip.text = String.format("CAP: %s", c.zip)
        } else {
            holder.zip.text = ""
        }
        if (c.birthday != null) {
            holder.birthday.text = String.format("Data di nascita: %s", c.birthday)
        } else {
            holder.birthday.text = ""
        }
        holder.modify.setOnClickListener {
            val i = Intent(context, AddCustomer::class.java)
            i.putExtra("customer", Gson().toJson(c))
            context.startActivity(i)
        }
        holder.delete.setOnClickListener {
            Dialog(
                context
            ).yesNoDialog("Cancellazione cliente",
                "Sei sicuro di voler eliminare " + c.name + " " + c.surname + "?",
                { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    val loader = Dialog(
                        context
                    ).showLoadingDialog("Elimino cliente...")
                    Thread {
                        val response: Boolean =
                            IsiAppActivity.isiCashierRequest!!.deleteCustomer(c)
                        context.runOnUiThread {
                            loader.dismiss()
                            if (response) {
                                context.updateUI()
                            } else {
                                Dialog(context).showErrorConnectionDialog(false)
                            }
                        }
                    }.start()
                },
                null
            )
        }
        if (searching) {
            holder.cardView.setOnClickListener {
                val i = Intent()
                i.putExtra("customer", Gson().toJson(c))
                context.setResult(Activity.RESULT_OK, i)
                context.finish()
            }
        }
    }

    override fun getItemCount(): Int {
        return customersFilteres.size
    }

    override fun getFilter(): Filter {
        return muaFilter
    }

    private val muaFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<Customer> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList.addAll(customers)
            } else {
                val filterPattern =
                    constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (item in customers) {
                    if (item.name.lowercase(Locale.getDefault()).contains(
                            filterPattern.lowercase(
                                Locale.getDefault()
                            )
                        ) || item.surname.lowercase(Locale.getDefault()).contains(
                            filterPattern.lowercase(
                                Locale.getDefault()
                            )
                        )
                    ) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            customersFilteres.clear()
            customersFilteres.addAll((results.values as ArrayList<Customer>))
            notifyDataSetChanged()
        }
    }

    init {
        this.searching = searching
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameSurnameCustomer: TextView
        var addressCustomer: TextView
        var fiscalCustomer: TextView
        var birthplace: TextView
        var zip: TextView
        var birthday: TextView
        var modify: Button
        var delete: Button
        var cardView: MaterialCardView

        init {
            nameSurnameCustomer = itemView.findViewById(R.id.nameProductList)
            addressCustomer = itemView.findViewById(R.id.addressCustomerList)
            fiscalCustomer = itemView.findViewById(R.id.fiscalCustomerList)
            birthplace = itemView.findViewById(R.id.birthplaceCustomerDisplay)
            zip = itemView.findViewById(R.id.zipCustomerDisplay)
            birthday = itemView.findViewById(R.id.birthdayCustomerDisplay)
            modify = itemView.findViewById(R.id.modifyCustomerButton)
            delete = itemView.findViewById(R.id.deleteCustomerButton)
            cardView = itemView.findViewById(R.id.cardViewLayout)
        }
    }
}