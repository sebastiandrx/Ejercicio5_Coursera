
package com.example.ejercicio1coursera

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MascotasDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "petagram.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE mascota (
                id INTEGER PRIMARY KEY,
                nombre TEXT NOT NULL,
                imagen TEXT NOT NULL,
                colorFondo INTEGER NOT NULL,
                rating INTEGER NOT NULL,
                ultimoRating INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Base de datos versión 1.
        // Se agregarán migraciones si cambia el esquema.
    }

    fun guardarMascota(mascota: Mascota) {
        val db = writableDatabase

        db.beginTransaction()
        try {
            // Calcula el siguiente orden de voto.
            val cursor = db.rawQuery(
                "SELECT COALESCE(MAX(ultimoRating), 0) " +
                        "FROM mascota",
                null
            )

            val nuevoOrden = cursor.use {
                it.moveToFirst()
                it.getLong(0) + 1L
            }

            val valores = ContentValues().apply {
                put("id", mascota.id)
                put("nombre", mascota.nombre)
                put("imagen", mascota.imagen)
                put("colorFondo", mascota.colorFondo)
                put("rating", mascota.rating)
                put("ultimoRating", nuevoOrden)
            }

            db.insertWithOnConflict(
                "mascota",
                null,
                valores,
                SQLiteDatabase.CONFLICT_REPLACE
            )

            // Conserva únicamente los últimos 5.
            db.execSQL(
                """
                DELETE FROM mascota
                WHERE id NOT IN (
                    SELECT id
                    FROM mascota
                    ORDER BY ultimoRating DESC
                    LIMIT 5
                )
                """.trimIndent()
            )

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun obtenerUltimasCinco(): List<Mascota> {
        val lista = mutableListOf<Mascota>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
            SELECT id, nombre, imagen,
                   colorFondo, rating, ultimoRating
            FROM mascota
            ORDER BY ultimoRating DESC
            LIMIT 5
            """.trimIndent(),
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Mascota(
                        id = it.getInt(0),
                        nombre = it.getString(1),
                        imagen = it.getString(2),
                        colorFondo = it.getInt(3),
                        rating = it.getInt(4),
                        ultimoRating = it.getLong(5)
                    )
                )
            }
        }

        return lista
    }

    fun obtenerRatingsGuardados(): Map<Int, Int> {
        val ratings = mutableMapOf<Int, Int>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT id, rating FROM mascota",
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                ratings[it.getInt(0)] = it.getInt(1)
            }
        }

        return ratings
    }
}
