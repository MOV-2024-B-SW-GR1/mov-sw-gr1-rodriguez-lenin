package com.example.myapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ConnectionClass(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mydb.db"
        private const val DATABASE_VERSION = 2

        // Constantes para la tabla Equipo
        const val TABLE_Equipo = "avion"
        const val COL_Equipo1_ID = "id"
        const val COL_Equipo_Nombre = "nombre"
        const val COL_Equipo_FechaCreacion = "fecha_fabricacion"
        const val COL_Equipo_Activo = "estado_operativo"
        const val COL_Equipo_NumeroJugadores = "capacidad_pasajeros"

        // Constantes para la tabla juegador
        const val TABLE_Jugador = "parte"
        const val COL_Jugador_ID = "id"
        const val COL_Equipo_ID = "avion_id"
        const val COL_Jugador_NOMBRE = "nombre"
        const val COL_Salario = "precio_repuesto"
        const val COL_Goles = "horas_uso"

        // Añadir nuevas constantes para las columnas de ubicación
        const val COL_Equipo_LATITUD = "latitud"
        const val COL_Equipo_LONGITUD = "longitud"
        const val COL_Equipo_UBICACION = "ubicacion"

         @Volatile
        private var instance: ConnectionClass? = null
        private var database: SQLiteDatabase? = null

        fun getInstance(context: Context): ConnectionClass {
            return instance ?: synchronized(this) {
                instance ?: ConnectionClass(context.applicationContext).also { instance = it }
            }
        }
        fun getConnection(context: Context): SQLiteDatabase {
            if (database?.isOpen != true) {
                database = getInstance(context).writableDatabase
            }
            return database!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("ConnectionClass", "Creating tables")
        // Modificar la tabla Equipo para incluir los nuevos campos
        db.execSQL("""      
            CREATE TABLE $TABLE_Equipo (      
                $COL_Equipo1_ID INTEGER PRIMARY KEY AUTOINCREMENT,      
                $COL_Equipo_Nombre TEXT NOT NULL,      
                $COL_Equipo_FechaCreacion TEXT NOT NULL,      
                $COL_Equipo_Activo INTEGER NOT NULL,      
                $COL_Equipo_NumeroJugadores INTEGER NOT NULL,
                $COL_Equipo_LATITUD REAL NOT NULL,
                $COL_Equipo_LONGITUD REAL NOT NULL,
                $COL_Equipo_UBICACION TEXT NOT NULL
            )      
        """.trimIndent())

        // Crear tabla Parte
        db.execSQL("""      
            CREATE TABLE $TABLE_Jugador (      
                $COL_Jugador_ID INTEGER PRIMARY KEY AUTOINCREMENT,      
                $COL_Equipo_ID INTEGER NOT NULL,      
                $COL_Jugador_NOMBRE TEXT NOT NULL,      
                $COL_Salario REAL NOT NULL,      
                $COL_Goles INTEGER NOT NULL,      
                FOREIGN KEY($COL_Equipo_ID) REFERENCES $TABLE_Equipo($COL_Equipo1_ID)      
            )      
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("ConnectionClass", "Upgrading database from version $oldVersion to $newVersion")
        
        if (oldVersion < 2) {
            // Añadir las nuevas columnas a la tabla existente
            db.execSQL("ALTER TABLE $TABLE_Equipo ADD COLUMN $COL_Equipo_LATITUD REAL NOT NULL DEFAULT 0.0")
            db.execSQL("ALTER TABLE $TABLE_Equipo ADD COLUMN $COL_Equipo_LONGITUD REAL NOT NULL DEFAULT 0.0")
            db.execSQL("ALTER TABLE $TABLE_Equipo ADD COLUMN $COL_Equipo_UBICACION TEXT NOT NULL DEFAULT ''")
        }
    }
}