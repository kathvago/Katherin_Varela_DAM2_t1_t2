package com.example.katherin_varela_dam2_t1_t2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.katherin_varela_dam2_t1_t2.model.Carrito
import com.example.katherin_varela_dam2_t1_t2.ui.ProductoAdapter
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar



class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarSecond)
        setSupportActionBar(toolbar)


        val txtTotal = findViewById<TextView>(R.id.txtTotal)
        val rvCarrito = findViewById<RecyclerView>(R.id.rvCarrito)

        rvCarrito.layoutManager = LinearLayoutManager(this)

        val carritoAdapter = ProductoAdapter(
            items = Carrito.items,
            onRemoveClick = { producto ->
                Carrito.items.remove(producto)
                rvCarrito.adapter?.notifyDataSetChanged()
                txtTotal.text = "Total: €%.2f".format(Carrito.total())
            }
        )
        rvCarrito.adapter = carritoAdapter


        txtTotal.text = "Total: €%.2f".format(Carrito.total())
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_second, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_confirmar -> {
                val total = Carrito.total()
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Enhorabuena, compra por valor de €%.2f realizada".format(total),
                    Snackbar.LENGTH_LONG
                ).show()
                true
            }

            R.id.action_vaciar -> {
                Carrito.clear()

                // refrescar lista + total
                findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvCarrito)
                    .adapter?.notifyDataSetChanged()
                findViewById<android.widget.TextView>(R.id.txtTotal).text = "Total: €0.00"

                Snackbar.make(
                    findViewById(android.R.id.content),
                    "carrito vaciado",
                    Snackbar.LENGTH_LONG
                ).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onResume() {
        super.onResume()

        findViewById<TextView>(R.id.txtTotal).text = "Total: €%.2f".format(Carrito.total())
        findViewById<RecyclerView>(R.id.rvCarrito).adapter?.notifyDataSetChanged()
    }
}
