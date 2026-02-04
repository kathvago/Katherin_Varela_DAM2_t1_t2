package com.example.katherin_varela_dam2_t1_t2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.katherin_varela_dam2_t1_t2.R
import com.example.katherin_varela_dam2_t1_t2.model.Producto
import com.bumptech.glide.Glide



class ProductoAdapter(
    private val items: MutableList<Producto>,
    private val onAddClick: ((Producto) -> Unit)? = null,
    private val onRemoveClick: ((Producto) -> Unit)? = null
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {


    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgProducto)
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombre)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecio)
        val btnAdd: Button = itemView.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = items[position]


        holder.txtNombre.text = producto.nombre
        holder.txtPrecio.text = "€%.2f".format(producto.precio)

        Glide.with(holder.itemView.context)
            .load(producto.imagenUrl)
            .into(holder.img)


        when {
            onRemoveClick != null -> {
                holder.btnAdd.visibility = View.VISIBLE
                holder.btnAdd.text = "Eliminar"
                holder.btnAdd.setOnClickListener { onRemoveClick.invoke(producto) }
            }

            onAddClick != null -> {
                holder.btnAdd.visibility = View.VISIBLE
                holder.btnAdd.text = "Añadir"
                holder.btnAdd.setOnClickListener { onAddClick.invoke(producto) }
            }

            else -> holder.btnAdd.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int = items.size
}
