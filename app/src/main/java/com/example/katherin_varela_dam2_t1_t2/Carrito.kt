package com.example.katherin_varela_dam2_t1_t2.model

object Carrito {

    val items: MutableList<Producto> = mutableListOf()

    fun add(producto: Producto) {
        items.add(producto)
    }

    fun clear() {
        items.clear()
    }

    fun total(): Double {
        return items.sumOf { it.precio }
    }
}