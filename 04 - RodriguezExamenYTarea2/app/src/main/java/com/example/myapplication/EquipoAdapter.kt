package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView

class EquipoAdapter(
    private val context: Context,
    private val equipos: List<Map<String, Any>>,
    private val showDeleteButton: Boolean,
    private val onItemClick: (Map<String, Any>) -> Unit,
    private val onDeleteClick: (Map<String, Any>) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = equipos.size
    override fun getItem(position: Int): Any = equipos[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_equipo, parent, false)

        val equipo = equipos[position]

        val tvNombreEquipo = view.findViewById<TextView>(R.id.tvNombreEquipo)
        val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)

        tvNombreEquipo.text = equipo["nombre"] as String

        btnDelete.visibility = if (showDeleteButton) View.VISIBLE else View.GONE

        view.setOnClickListener { onItemClick(equipo) }
        btnDelete.setOnClickListener { onDeleteClick(equipo) }

        return view
    }
}