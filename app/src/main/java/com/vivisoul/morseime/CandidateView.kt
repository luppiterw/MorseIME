package com.vivisoul.morseime

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.view.View
import java.util.ArrayList

/**
 * Created by Hughie on 2017/6/10.
 *
 */
class CandidateView///< 覆盖了onDraw函数应清除该标记
(context: Context) : View(context) {
    private var mSuggestions: List<String>? = null      // 存放候选列表
    private val X_GAP = 10    // 每个候选之间的间隔
    private var mColorNormal: Int = 0               // 候选前景色
    private var mVerticalPadding: Int = 0           // 候选文字上下边距
    private var mPaint: Paint? = null                   // 用于绘制候选

    init {

        Log.d(this.javaClass.toString(), "CandidateView.init ")
        val res = context.resources
        setBackgroundColor(resources.getColor(R.color.candidate_background))
        mColorNormal = res.getColor(R.color.candidate_normal)
        mVerticalPadding = res.getDimensionPixelSize(R.dimen.candidate_vertical_padding)
        mPaint = Paint()
        mPaint!!.color = mColorNormal
        mPaint!!.isAntiAlias = true
        mPaint!!.textSize = res.getDimensionPixelSize(R.dimen.candidate_font_height).toFloat()
        mPaint!!.strokeWidth = 0F
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(this.javaClass.toString(), "CandidateView.onMessage  ")

        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val wSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val measuredWidth = View.resolveSize(50, widthMeasureSpec)
        val desiredHeight = mPaint!!.getTextSize().toInt() + mVerticalPadding

        // Maximum possible width and desired height
        setMeasuredDimension(measuredWidth, View.resolveSize(desiredHeight, heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {
        Log.d(this.javaClass.toString(), "CandidateView.onDraw ")
        super.onDraw(canvas)

        var x = 0
        val count = mSuggestions!!.size
        val height = height
        val y = ((height - mPaint!!.getTextSize()) / 2 - mPaint!!.ascent()).toInt()

        for (i in 0..count - 1) {
            val suggestion = mSuggestions!!.get(i)
            val textWidth = mPaint!!.measureText(suggestion)
            val wordWidth = textWidth.toInt() + X_GAP * 2

            canvas.drawText(suggestion, (x + X_GAP).toFloat(), y.toFloat(), mPaint)
            x += wordWidth
        }
    }

    fun setSuggestions(suggestions: List<String>?) {
        if (suggestions != null) {
            mSuggestions = ArrayList(suggestions)
        }
        invalidate()
        requestLayout()
    }

}

