package com.liaobusi.uicore

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context

import android.graphics.Matrix
import android.graphics.PointF

import android.util.AttributeSet

import android.widget.ImageView
import com.liaobusi.uicore.utils.PointFEvaluator
import kotlin.math.max


class AnimatedScrollImageView @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    var autoAnimate = true

    var animateDuration: Long = 10000

    private var mAnimator: Animator? = null

    private val pointEvaluator = PointFEvaluator()

    init {
        super.setScaleType(ScaleType.MATRIX)
    }

    override fun setScaleType(scaleType: ScaleType?) {
        throw IllegalStateException("AnimatedScrollImageView no support setScaleType()")
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val drawableWidth = drawable?.intrinsicWidth?.toFloat() ?: 0f
        val drawableHeight = drawable?.intrinsicHeight?.toFloat() ?: 0f
        if (drawableHeight == 0f || drawableWidth == 0f) {
            return
        }

        val vwidth = w - paddingLeft - paddingRight
        val vheight = h - paddingTop - paddingBottom
        val scale = if (drawableWidth <= vwidth && drawableHeight <= vheight) {
            1.0f
        } else {
            max(vwidth.toFloat() / drawableWidth, vheight.toFloat() / drawableHeight)
        }
        val horizontalScrollRange = (drawableWidth * scale - vwidth)
        val verticalScrollRange = (drawableHeight * scale - vheight)
        val startPoint = PointF(0f, 0f)
        val endPoint = PointF(-horizontalScrollRange, -verticalScrollRange)
        mAnimator?.cancel()
        mAnimator = ValueAnimator.ofObject(pointEvaluator, startPoint, endPoint).apply {
            duration = animateDuration
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                val value = it.animatedValue as PointF
                val newMatrix = Matrix()
                newMatrix.setScale(scale, scale)
                newMatrix.postTranslate(value.x, value.y)
                imageMatrix = newMatrix
            }
        }
        if (autoAnimate) {
            mAnimator?.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAnimator?.cancel()
    }
}


