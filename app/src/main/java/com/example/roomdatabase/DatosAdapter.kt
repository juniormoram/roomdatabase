package com.example.roomdatabase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DatosAdapter (val datos: List<CapaDatos.ProductoEntity>,
                    val checkTask: (CapaDatos.ProductoEntity) -> Unit,
                    val deleteTask: (CapaDatos.ProductoEntity) -> Unit) : RecyclerView.Adapter<DatosAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = datos[position]
        holder.bind(item, checkTask, deleteTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_task, parent, false))
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreproducto = view.findViewById<TextView>(R.id.nombre)
        val cantidadproducto = view.findViewById<TextView>(R.id.cantidad)
        val precioproducto = view.findViewById<TextView>(R.id.precio)
        val preciofinal = view.findViewById<TextView>(R.id.preciototal)
        val botonelimina = view.findViewById(R.id.btnelimina) as Button


        fun bind(datos: CapaDatos.ProductoEntity, checkTask: (CapaDatos.ProductoEntity) -> Unit, deleteTask: (CapaDatos.ProductoEntity) -> Unit) {

            nombreproducto.text = datos.nombreprod
            cantidadproducto.text = datos.cantidad.toString()
            precioproducto.text = datos.precio.toString()
            preciofinal.text = datos.preciototal.toString()


            botonelimina.setOnClickListener { deleteTask(datos) }
        }
    }

}