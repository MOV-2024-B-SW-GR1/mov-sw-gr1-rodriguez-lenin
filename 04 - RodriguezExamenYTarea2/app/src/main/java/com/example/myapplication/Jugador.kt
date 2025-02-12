package com.example.myapplication

import android.app.AlertDialog
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityParteEquipoBinding

class Jugador : AppCompatActivity() {
    private lateinit var binding: ActivityParteEquipoBinding
    private lateinit var db: SQLiteDatabase
    private var pId: Int = 0
    private var aId: Int = 0
    private var msg: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParteEquipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ConnectionClass.getConnection(this)
        val i = intent
        msg = i.getStringExtra("msg").toString()
        pId = i.getIntExtra("pId", 0)
        aId = i.getIntExtra("aId", 0)

        when (msg) {
            "add" -> {
                binding.btnSave.text = "Guardar"
                binding.btnSave.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.GONE
                cargarEquipo()
            }
            "edit_partes" -> {
                binding.btnSave.text = "Actualizar"
                binding.btnSave.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.GONE
                cargarDatosParte()
            }
            "delete_Jugadores" -> {
                binding.btnSave.visibility = View.GONE
                binding.btnDelete.text = "Eliminar"
                binding.btnDelete.visibility = View.VISIBLE
                cargarDatosParte()
                bloquearCampos() // Bloquear los campos en modo "delete"
            }
        }

        binding.btnSave.setOnClickListener {
            when (msg) {
                "add" -> insertData()
                "edit_partes" -> updateData()
            }
        }

        binding.btnDelete.setOnClickListener {
            if (msg == "delete_Jugadores") {
                deleteData()
            }
        }
    }

    private fun cargarEquipo() {
        val cursor = db.rawQuery("SELECT id, nombre FROM avion", null)
        val equipos = mutableListOf<String>()
        val equipoIds = mutableListOf<Int>()

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(ConnectionClass.COL_Equipo1_ID))
                val nombre = it.getString(it.getColumnIndexOrThrow(ConnectionClass.COL_Equipo_Nombre))
                equipos.add(nombre)
                equipoIds.add(id)
            }
        }

        if (equipos.isNotEmpty()) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, equipos)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerEquipos.adapter = adapter

            binding.spinnerEquipos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    aId = equipoIds[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    aId = 0
                }
            }
        } else {
            Toast.makeText(this, "No hay equipos disponibles", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun cargarDatosParte() {
        val cursor = db.rawQuery("SELECT * FROM parte WHERE id = ?", arrayOf(pId.toString()))
        cursor.use {
            if (it.moveToFirst()) {
                aId = it.getInt(it.getColumnIndexOrThrow(ConnectionClass.COL_Equipo_ID))
                binding.etNombreJugador.setText(it.getString(it.getColumnIndexOrThrow(ConnectionClass.COL_Jugador_NOMBRE)))
                binding.etSalario.setText(it.getDouble(it.getColumnIndexOrThrow(ConnectionClass.COL_Salario)).toString())
                binding.etgoles.setText(it.getInt(it.getColumnIndexOrThrow(ConnectionClass.COL_Goles)).toString())

                val equipoCursor = db.rawQuery("SELECT id, nombre FROM avion", null)
                equipoCursor.use { equipoIt ->
                    val equipos = mutableListOf<String>()
                    val equipoIds = mutableListOf<Int>()
                    while (equipoIt.moveToNext()) {
                        val id = equipoIt.getInt(equipoIt.getColumnIndexOrThrow(ConnectionClass.COL_Equipo1_ID))
                        val nombre = equipoIt.getString(equipoIt.getColumnIndexOrThrow(ConnectionClass.COL_Equipo_Nombre))
                        equipos.add(nombre)
                        equipoIds.add(id)
                    }

                    if (equipos.isNotEmpty()) {
                        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, equipos)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerEquipos.adapter = adapter

                        val equipoPosition = equipoIds.indexOf(aId)
                        if (equipoPosition != -1) {
                            binding.spinnerEquipos.setSelection(equipoPosition)
                        }
                    }
                }
            }
        }
    }

    private fun deleteData() {
        // Mostrar un cuadro de diálogo de confirmación
        AlertDialog.Builder(this)
            .setTitle("Eliminar Parte")
            .setMessage("¿Estás seguro de que deseas eliminar esta parte?")
            .setPositiveButton("Sí") { _, _ ->
                val selection = "${ConnectionClass.COL_Jugador_ID} = ?"
                val selectionArgs = arrayOf(pId.toString())

                val count = db.delete(ConnectionClass.TABLE_Jugador, selection, selectionArgs)
                if (count > 0) {
                    Toast.makeText(this, "Parte del avión eliminada exitosamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al eliminar jugador del equipo", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun bloquearCampos() {
        binding.etNombreJugador.isEnabled = false
        binding.etSalario.isEnabled = false
        binding.etgoles.isEnabled = false
        binding.spinnerEquipos.isEnabled = false
    }

    private fun insertData() {
        val nombreParte = binding.etNombreJugador.text.toString()
        val precioRepuesto = binding.etSalario.text.toString().toDoubleOrNull() ?: 0.0
        val horasUso = binding.etgoles.text.toString().toIntOrNull() ?: 0

        if (aId == 0) {
            Toast.makeText(this, "Debe seleccionar una equipo", Toast.LENGTH_SHORT).show()
            return
        }

        val values = ContentValues().apply {
            put(ConnectionClass.COL_Equipo_ID, aId)
            put(ConnectionClass.COL_Jugador_NOMBRE, nombreParte)
            put(ConnectionClass.COL_Salario, precioRepuesto)
            put(ConnectionClass.COL_Goles, horasUso)
        }

        val newRowId = db.insert(ConnectionClass.TABLE_Jugador, null, values)
        if (newRowId > 0) {
            Toast.makeText(this, " equipo insertado exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al insertar un equipo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData() {
        val nombreParte = binding.etNombreJugador.text.toString()
        val precioRepuesto = binding.etSalario.text.toString().toDoubleOrNull() ?: 0.0
        val horasUso = binding.etgoles.text.toString().toIntOrNull() ?: 0

        val values = ContentValues().apply {
            put(ConnectionClass.COL_Jugador_NOMBRE, nombreParte)
            put(ConnectionClass.COL_Salario, precioRepuesto)
            put(ConnectionClass.COL_Goles, horasUso)
        }

        val selection = "${ConnectionClass.COL_Jugador_ID} = ?"
        val selectionArgs = arrayOf(pId.toString())

        val count = db.update(ConnectionClass.TABLE_Jugador, values, selection, selectionArgs)
        if (count > 0) {
            Toast.makeText(this, "juegador del equipo actualizado exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar un Equipo", Toast.LENGTH_SHORT).show()
        }
    }


}