package com.example.katherin_varela_dam2_t1_t2

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.katherin_varela_dam2_t1_t2.model.Carrito
import com.example.katherin_varela_dam2_t1_t2.model.Producto
import com.example.katherin_varela_dam2_t1_t2.ui.ProductoAdapter
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var productos: MutableList<Producto>
    private lateinit var adapter: ProductoAdapter
    private lateinit var queue: RequestQueue

    private var badgeTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)



        // RecyclerView
        val rvItems = findViewById<RecyclerView>(R.id.rvItems)
        rvItems.layoutManager = LinearLayoutManager(this)

        productos = mutableListOf()

        adapter = ProductoAdapter(
            items = productos,
            onAddClick = { producto ->
                Carrito.add(producto)
                Toast.makeText(this, "Añadido al carrito: ${producto.nombre}", Toast.LENGTH_SHORT).show()
                actualizarBadge()
            }
        )


        rvItems.adapter = adapter

        // Volley
        queue = Volley.newRequestQueue(this)

        // Spinner categorías
        val spinner = findViewById<Spinner>(R.id.spCategorias)
        cargarCategorias(spinner)


        cargarProductos("https://dummyjson.com/products/category/beauty")
    }

    // CATEGORÍAS
    private fun cargarCategorias(spinner: Spinner) {
        val urlCategorias = "https://dummyjson.com/products/categories"

        val requestCategorias = JsonArrayRequest(
            Request.Method.GET,
            urlCategorias,
            null,
            { response ->
                val categorias = mutableListOf<String>()

                for (i in 0 until response.length()) {
                    val item = response.get(i)
                    if (item is JSONObject) {
                        categorias.add(item.getString("name"))
                    } else {
                        categorias.add(response.getString(i))
                    }
                }

                val spinnerAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    categorias
                )
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = spinnerAdapter

                spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: android.widget.AdapterView<*>,
                        view: android.view.View?,
                        position: Int,
                        id: Long
                    ) {
                        val categoria = parent.getItemAtPosition(position)
                            .toString()
                            .lowercase()
                            .replace(" ", "-")

                        val urlFiltrada = "https://dummyjson.com/products/category/$categoria"
                        cargarProductos(urlFiltrada)
                    }

                    override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
                }
            },
            { error ->
                Toast.makeText(this, "Error cargando categorías: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(requestCategorias)
    }


    private fun cargarProductos(url: String) {
        val requestProductos = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val array = response.getJSONArray("products")
                productos.clear()

                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)

                    val nombre = obj.getString("title")
                    val precio = obj.getDouble("price")
                    val imagenUrl = obj.getString("thumbnail")

                    productos.add(Producto(nombre, precio, imagenUrl))
                }

                adapter.notifyDataSetChanged()
            },
            { error ->
                Toast.makeText(this, "Error cargando productos: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(requestProductos)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu.findItem(R.id.action_ver_carrito)
        val actionView = item.actionView

        badgeTextView = actionView?.findViewById(R.id.txtBadge)

        actionView?.setOnClickListener {
            val intent = android.content.Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        actualizarBadge()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ver_carrito -> {
                val intent = android.content.Intent(this, SecondActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun actualizarBadge() {
        val count = Carrito.items.size

        badgeTextView?.let { badge ->
            if (count > 0) {
                badge.visibility = View.VISIBLE
                badge.text = count.toString()
            } else {
                badge.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarBadge()
    }
}
