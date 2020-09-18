package com.anwesh.uiprojects.dividemiddleballview

/**
 * Created by anweshmishra on 18/09/20.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.app.Activity
import android.content.Context

val parts : Int = 3
val colors : Array<Int> = arrayOf(
        "#3F51B5",
        "#03A9F4",
        "#4CAF50",
        "#FF5722",
        "#FFEB3B"
).map({Color.parseColor(it)}).toTypedArray()
val scGap : Float = 0.02f / parts
val strokeFactor : Float = 90f
val rFactor : Float = 14.2f
val foreColor : Int = Color.parseColor("#BDBDBD")
val delay : Long = 20
val xFactor : Float = 0.35f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()
