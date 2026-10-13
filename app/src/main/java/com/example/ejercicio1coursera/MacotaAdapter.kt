
package com.example.ejercicio1coursera

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

fun Context.dp(value: Int): Int =
    (value * resources.displayMetrics.density).toInt()

fun fondoRedondeado(
    color: Int,
    radio: Float = 18f
): GradientDrawable =
    GradientDrawable().apply {
        setColor(color)
        cornerRadius = radio
    }


class MascotaAdapter(
    private val mascotas: List<Mascota>,
    private val permitirVotos: Boolean = true,
    private val onVotar: ((Mascota) -> Unit)? = null
) : RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder>() {


    class MascotaViewHolder(
        val contenedor: LinearLayout,
        val imagen: TextView,
        val nombre: TextView,
        val contador: TextView,
        val votar: BoneView
    ) : RecyclerView.ViewHolder(contenedor)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MascotaViewHolder {

        val ctx = parent.context

        val contenedor = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(ctx.dp(10), ctx.dp(10), ctx.dp(10), ctx.dp(8))
            background = fondoRedondeado(Color.WHITE, ctx.dp(16).toFloat())
            elevation = ctx.dp(3).toFloat()
        }

        val imagen = TextView(ctx).apply {
            textSize = 100f
            gravity = Gravity.CENTER
        }

        contenedor.addView(
            imagen,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ctx.dp(240)
            )
        )

        val fila = LinearLayout(ctx).apply {
            gravity = Gravity.CENTER_VERTICAL
            orientation = LinearLayout.HORIZONTAL
            setPadding(4, ctx.dp(6), 4, 0)
        }

        val votar = BoneView(ctx, Color.WHITE).apply {
            contentDescription = "Votar por mascota"
        }
        fila.addView(votar)

        val nombre = TextView(ctx).apply {
            textSize = 17f
            setTextColor(Color.DKGRAY)
            typeface = Typeface.DEFAULT_BOLD
            setPadding(ctx.dp(8), 0, 0, 0)
        }
        fila.addView(
            nombre,
            LinearLayout.LayoutParams(0, ctx.dp(40), 1f)
        )

        val contador = TextView(ctx).apply {
            textSize = 18f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
        }
        fila.addView(contador)

        val huesoAmarillo = BoneView(
            ctx, Color.rgb(255, 193, 7)
        )
        fila.addView(huesoAmarillo)

        contenedor.addView(fila)

        return MascotaViewHolder(
            contenedor, imagen, nombre, contador, votar
        )
    }

    override fun onBindViewHolder(
        holder: MascotaViewHolder,
        position: Int
    ) {
        val mascota = mascotas[position]

        holder.imagen.text = mascota.imagen
        holder.imagen.background =
            fondoRedondeado(mascota.colorFondo, 18f)

        holder.nombre.text = mascota.nombre
        holder.contador.text = mascota.rating.toString()

        holder.votar.visibility =
            if (permitirVotos) View.VISIBLE else View.INVISIBLE

        holder.votar.setOnClickListener(null)


        if (permitirVotos) {
            holder.votar.setOnClickListener {
                mascota.rating += 1

                holder.contador.text = mascota.rating.toString()

                onVotar?.invoke(mascota.copy())
            }
        }

    }

    override fun getItemCount(): Int = mascotas.size
}
