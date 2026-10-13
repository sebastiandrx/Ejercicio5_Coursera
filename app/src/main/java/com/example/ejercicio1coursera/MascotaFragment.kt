
package com.example.ejercicio1coursera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MascotasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contexto = requireContext()

        val lista = RecyclerView(contexto).apply {
            layoutManager = LinearLayoutManager(contexto)

            adapter = MascotaAdapter(
                mascotas = MascotasData.mascotas,
                permitirVotos = true,
                onVotar = { mascota ->
                    MascotasRepository.guardarVoto(
                        contexto,
                        mascota
                    )
                }
            )

            setPadding(
                contexto.dp(14),
                contexto.dp(12),
                contexto.dp(14),
                contexto.dp(20)
            )

            clipToPadding = false
        }

        // Recupera los ratings de las mascotas
        // que aún están guardadas en SQLite.
        MascotasRepository.cargarRatings(contexto) { ratings ->
            if (view != null || isAdded) {
                MascotasData.mascotas.forEach { mascota ->
                    ratings[mascota.id]?.let {
                        mascota.rating = it
                    }
                }

                lista.adapter?.notifyDataSetChanged()
            }
        }

        return lista
    }
}
