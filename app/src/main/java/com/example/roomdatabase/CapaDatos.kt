package com.example.roomdatabase

import android.app.Application
import androidx.room.*

class CapaDatos {

    @Entity(tableName = "Producto")
    data class ProductoEntity(
        @PrimaryKey()
        var id: Int = 0,
        var nombreprod: String = "",
        var cantidad: Int = 0,
        var precio: Int = 0,
        var preciototal: Int = cantidad * precio

    )

    @Dao
    interface TaskDao {
        @Query("SELECT * FROM Producto")
        fun getAllTasks(): MutableList<ProductoEntity>

        @Insert
        fun addTask(taskEntity: ProductoEntity):Long

        @Query("SELECT * FROM Producto where id like :id")
        fun getTaskById(id: Long): ProductoEntity

        @Update
        fun updateTask(taskEntity: ProductoEntity): Int

        @Delete
        fun deleteTask(taskEntity: ProductoEntity): Int

        @Query("SELECT NULLIF(max(id),0) FROM Producto")
        fun getMaxTaskid(): Int
    }

    @Database(entities = arrayOf(ProductoEntity::class), version = 1)
    abstract class TasksDatabase : RoomDatabase() {
        abstract fun taskDao(): TaskDao
    }

    class MisNotasApp : Application() {

        companion object {
            lateinit var database: TasksDatabase
        }

        override fun onCreate() {
            super.onCreate()
            MisNotasApp.database = Room.databaseBuilder(this, TasksDatabase::class.java, "tasks-db").build()
        }
    }
}