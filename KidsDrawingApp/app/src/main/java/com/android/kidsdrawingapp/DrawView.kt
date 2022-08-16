package com.android.kidsdrawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 10.toFloat()

    private var color = Color.BLACK

    private var canvas: Canvas? = null
    private val mPaths = ArrayList<CustomPath>()
    private val mUndoPaths = ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)

        mDrawPaint?.let {
            it.color = color
            it.style = Paint.Style.STROKE
            it.strokeJoin = Paint.Join.ROUND
            it.strokeCap = Paint.Cap.ROUND
        }

        mCanvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8)
        canvas = Canvas(mCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        mCanvasBitmap?.let {
            canvas?.drawBitmap(it, 0f, 0f, mCanvasPaint)
        }

        for (p in mPaths) {
            mDrawPaint?.let {
                it.strokeWidth = p.brushThickness
                it.color = p.color
                canvas?.drawPath(p, it)
            }
        }

        mDrawPaint?.let { paint ->
            mDrawPath?.let { customPath ->
                if (!customPath.isEmpty) {
                    paint.strokeWidth = customPath.brushThickness
                    paint.color = customPath.color
                    canvas?.drawPath(customPath, paint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { ev ->
            val touchX = ev.x;
            val touchY = ev.y;

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    mDrawPath?.let { customPath ->
                        customPath.color = color
                        customPath.brushThickness = mBrushSize
                        customPath.reset()
                        customPath.moveTo(touchX, touchY)
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    mDrawPath?.lineTo(touchX, touchY)
                }

                MotionEvent.ACTION_UP -> {
                    mDrawPath?.let {
                        mPaths.add(it)
                    }
                    mDrawPath = CustomPath(color, mBrushSize)
                }
                else -> return false
            }
        }
        invalidate()
        return true
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path()
}