package com.example.roomdatabase

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class
MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var datos: MutableList<CapaDatos.ProductoEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        datos = ArrayList()

        getTasks()
        Log.i("Datos vacios", datos.toString());
        btn_Añadir.setOnClickListener {
            addTask(
                CapaDatos.ProductoEntity(
                    nombreprod = txt_Nombre.text.toString(),
                    id = datos.size,
                    cantidad = txt_Cantidad.text.toString().toInt(),
                    precio = txt_Precio.text.toString().toInt()
                )
            )
        }

    }
    fun getTasks() {
        doAsync {
            datos = CapaDatos.MisNotasApp.database.taskDao().getAllTasks()
            Log.i("Datos rellanados ", datos.toString());
            uiThread {
                Log.i("Datos rellenados", datos.toString());
                setUpRecyclerView(datos)
            }
        }
    }
    fun addTask(task: CapaDatos.ProductoEntity) {
        doAsync {
            val id = CapaDatos.MisNotasApp.database.taskDao().addTask(task)
            val recoveryTask = CapaDatos.MisNotasApp.database.taskDao().getTaskById(id)
            uiThread {
                datos.add(recoveryTask)
                Log.i("Datos ingresados", datos.toString());
                adapter.notifyItemInserted(datos.size)
                clearFocus()
                hideKeyboard()
            }
        }
    }

    lateinit var adapter: DatosAdapter

    fun setUpRecyclerView(tasks: List<CapaDatos.ProductoEntity>) {
        adapter = DatosAdapter(tasks, { updateTask(it) }, { deleteTask(it) })
        recyclerView = findViewById(R.id.recyclerDatos)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    fun clearFocus() {
        txt_Nombre.setText("")
        txt_Cantidad.setText("")
        txt_Precio.setText("")
    }

    fun Context.hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    fun updateTask(task: CapaDatos.ProductoEntity) {
        doAsync {
            CapaDatos.MisNotasApp.database.taskDao().updateTask(task)
        }
    }

    fun deleteTask(task: CapaDatos.ProductoEntity) {
        doAsync {
            val position = datos.indexOf(task)
            CapaDatos.MisNotasApp.database.taskDao().deleteTask(task)
            datos.remove(task)
            uiThread {
                adapter.notifyItemRemoved(position)
            }
        }
    }
}