package com.liaobusi.uicore.utils

import android.animation.TypeEvaluator
import android.graphics.Point
import android.graphics.PointF

class PointFEvaluator : TypeEvaluator<PointF> {


    private var mPoint: PointF? = null


    constructor()


    constructor(reuse: PointF) {
        mPoint = reuse
    }


    override fun evaluate(fraction: Float, startValue: PointF, endValue: PointF): PointF {
        val x = startValue.x + fraction * (endValue.x - startValue.x)
        val y = startValue.y + fraction * (endValue.y - startValue.y)
        return if (mPoint == null) {
            PointF(x, y)
        } else {
            mPoint!!.set(x, y)
            return mPoint!!
        }
    }
}