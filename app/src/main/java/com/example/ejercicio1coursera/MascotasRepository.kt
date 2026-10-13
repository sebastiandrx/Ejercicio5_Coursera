
package com.example.ejercicio1coursera

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.Executors

object MascotasRepository {

    private val ejecutor = Executors.newSingleThreadExecutor()
    private val principal = Handler(Looper.getMainLooper())

    fun guardarVoto(
        contexto: Context,
        mascota: Mascota
    ) {
        val appContext = contexto.applicationContext
        val copia = mascota.copy()

        ejecutor.execute {
            try {
                MascotasDatabaseHelper(appContext).use { db ->
                    db.guardarMascota(copia)
                }
            } catch (e: Exception) {
                Log.e("PetagramDB", "Error guardando voto", e)
            }
        }
    }

    fun cargarFavoritas(
        contexto: Context,
        resultado: (List<Mascota>) -> Unit
    ) {
        val appContext = contexto.applicationContext

        ejecutor.execute {
            val mascotas = try {
                MascotasDatabaseHelper(appContext).use { db ->
                    db.obtenerUltimasCinco()
                }
            } catch (e: Exception) {
                Log.e("PetagramDB", "Error leyendo favoritas", e)
                emptyList()
            }

            principal.post {
                resultado(mascotas)
            }
        }
    }

    fun cargarRatings(
        contexto: Context,
        resultado: (Map<Int, Int>) -> Unit
    ) {
        val appContext = contexto.applicationContext

        ejecutor.execute {
            val ratings = try {
                MascotasDatabaseHelper(appContext).use { db ->
                    db.obtenerRatingsGuardados()
                }
            } catch (e: Exception) {
                Log.e("PetagramDB", "Error leyendo ratings", e)
                emptyMap()
            }

            principal.post {
                resultado(ratings)
            }
        }
    }
}
