
package com.example.ejercicio1coursera

data class Mascota(
    val id: Int,
    val nombre: String,
    val imagen: String,
    val colorFondo: Int,
    var rating: Int,
    var ultimoRating: Long = 0L
)

object MascotasData {

    val mascotas = mutableListOf(
        Mascota(1, "Catty", "🐕", 0xFFFF80AB.toInt(), 5),
        Mascota(2, "Ronny", "🐶", 0xFF42A5F5.toInt(), 3),
        Mascota(3, "Luna", "🐩", 0xFFFFCC80.toInt(), 8),
        Mascota(4, "Max", "🐕", 0xFFA5D6A7.toInt(), 2),
        Mascota(5, "Rocky", "🐶", 0xFFCE93D8.toInt(), 7),
        Mascota(6, "Toby", "🐕", 0xFF80CBC4.toInt(), 4),
        Mascota(7, "Bella", "🐩", 0xFFFFAB91.toInt(), 6)
    )

    // Para la segunda Activity:
    // cinco mascotas de ejemplo, independientes de los votos.
    val favoritasDummy: List<Mascota>
        get() = listOf(
            mascotas[1],
            mascotas[0],
            mascotas[2],
            mascotas[4],
            mascotas[3]
        ).map { it.copy() }
}
