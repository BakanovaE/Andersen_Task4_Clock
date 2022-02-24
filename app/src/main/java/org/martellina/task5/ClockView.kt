package org.martellina.task5

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.lang.Math.*
import java.util.*


class ClockView @JvmOverloads constructor( context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var radius = 0.0f
    private var centerX = 0f
    private var centerY = 0f
    private lateinit var paint: Paint
    private lateinit var path: Path
    private lateinit var rect: Rect
    private var padding = 100
    private var isInitialized = false
    private var hour = 0f
    private var minute = 0f
    private var second = 0f
    private var hourHandSize = 0
    private var handSize = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var watchColor: Int
    private var hourColor: Int
    private var minuteColor: Int
    private var secondColor: Int
    private var strokeWidth: Float
    private var hourWidth: Float
    private var minuteWidth: Float
    private var secondWidth: Float

    init {
        val attrArray = context.obtainStyledAttributes(attrs, R.styleable.ClockView)
        watchColor = attrArray.getColor(R.styleable.ClockView_watchColor, Color.BLACK)
        strokeWidth = attrArray.getDimension(R.styleable.ClockView_strokeWidth, 8f)
        hourWidth = attrArray.getDimension(R.styleable.ClockView_handHourWidth, 8f)
        minuteWidth = attrArray.getDimension(R.styleable.ClockView_handMinuteWidth, 8f)
        secondWidth = attrArray.getDimension(R.styleable.ClockView_handSecondWidth, 8f)
        hourColor = attrArray.getColor(R.styleable.ClockView_handHourColor, Color.BLACK)
        minuteColor = attrArray.getColor(R.styleable.ClockView_handMinuteColor, Color.BLACK)
        secondColor = attrArray.getColor(R.styleable.ClockView_handSecondColor, Color.BLACK)
        attrArray.recycle()
    }

    private fun initValues() {
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        val min = min(width, height)
        radius = min / 2f - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7

        paint = Paint()
        path = Path()
        rect = Rect()

        hourHandSize = (radius - radius / 2).toInt()
        handSize = (radius - radius / 4).toInt()
        isInitialized = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isInitialized) {
            initValues()
        }

        drawCircle(canvas)
        drawPoints(canvas)
        drawHands(canvas)

        postInvalidateDelayed(500)

        invalidate()
    }

    private fun setPaintAttributes(c: Int, sWidth: Float) {
        paint.apply {
            reset()
            color = c
            style = Paint.Style.STROKE
            strokeWidth = sWidth
            isAntiAlias = true
        }
    }

    private fun drawCircle(canvas: Canvas?) {
        paint.reset()
        setPaintAttributes(watchColor, strokeWidth)
        canvas?.drawCircle(centerX, centerY, radius + 20, paint)
    }

    private fun drawPoints(canvas: Canvas?) {
        setPaintAttributes(watchColor, strokeWidth)
        for (i in 1..12) {
            canvas?.drawLine(
                    centerX,
                    centerY - radius,
                    centerX, centerY - radius + 10f,
                    paint
            )
            canvas?.rotate((360 / 12).toFloat(), centerX, centerY)
        }
    }

    private fun drawHands(canvas: Canvas?) {
        val time = Calendar.getInstance()
        hour = time[Calendar.HOUR_OF_DAY].toFloat()
        minute = time[Calendar.MINUTE].toFloat()
        second = time[Calendar.SECOND].toFloat()
        drawHandHour(canvas, (hour + time.get(Calendar.MINUTE) / 60.0) * 5f)
        drawHandMinute(canvas, (minute + time.get(Calendar.SECOND) / 60.0))
        drawHandSecond(canvas, (time.get(Calendar.SECOND)).toDouble())

    }


    private fun drawHandMinute(canvas: Canvas?, location: Double) {
        val angle = PI * location / 30 - PI / 2
        setPaintAttributes(minuteColor, minuteWidth)
        val handRadius = radius - handTruncation
        canvas?.drawLine(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2 + cos(angle) * handRadius).toFloat(),
            (height / 2 + sin(angle) * handRadius).toFloat(),
            paint
        )
    }

    private fun drawHandSecond(canvas: Canvas?, location: Double) {
        val angle = PI * location / 30 - PI / 2
        setPaintAttributes(secondColor, secondWidth)
        val handRadius = radius - handTruncation
        canvas?.drawLine(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2 + cos(angle) * handRadius).toFloat(),
            (height / 2 + sin(angle) * handRadius).toFloat(),
            paint
        )
    }

    private fun drawHandHour(canvas: Canvas?, location: Double) {
        val angle = PI * location / 30 - PI / 2
        setPaintAttributes(hourColor, hourWidth)
        val handRadius = radius - handTruncation - hourHandTruncation
        canvas?.drawLine((width / 2).toFloat(),
                (height / 2).toFloat(),
                (width / 2 + cos(angle) * handRadius).toFloat(),
                (height / 2 + sin(angle) * handRadius).toFloat(),
                paint
        )
    }
}