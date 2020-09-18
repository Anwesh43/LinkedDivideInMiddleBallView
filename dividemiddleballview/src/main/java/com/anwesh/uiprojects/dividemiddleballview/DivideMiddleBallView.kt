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

val parts : Int = 5
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
val yFactor : Float = 0.3f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawDivideBallView(scale : Float, w : Float, h : Float, paint : Paint) {
    val sc1 : Float = scale.divideScale(0, parts)
    val sc2 : Float = scale.divideScale(1, parts)
    val sc3 : Float = scale.divideScale(2, parts)
    val sc4 : Float = scale.divideScale(3, parts)
    val sc5 : Float = scale.divideScale(4, parts)
    val r : Float = Math.min(w, h) / rFactor
    val currX : Float = (w - 2 * r) * xFactor
    val wLeft : Float = w - 2 * currX
    val x : Float = currX * sc2 + (wLeft) * sc3 + currX * sc4
    val y : Float = yFactor * h * sc3.sinify()
    save()
    translate(w / 2, h / 2)
    for (j in 0..1) {
        save()
        scale(1f, 1f - 2 * j)
        drawCircle(x, y, r * (sc1 - sc5), paint)
        restore()
    }
    restore()
}

fun Canvas.drawDMBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = colors[i]
    drawDivideBallView(scale, w, h, paint)
}

class DivideMiddleBallView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += dir * scGap
            if (Math.abs(scale - prevScale) > 1f) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class DMBNode(var i : Int, val state : State = State()) {

        private var next : DMBNode? = null
        private var prev : DMBNode? = null

        init {
           addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = DMBNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawDMBNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : DMBNode {
            var curr : DMBNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }
}
