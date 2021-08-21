package com.hlw.demo.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MoveView : View {
    var lastX: Int = 0
    var lastY: Int = 0

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 获取手指触摸点的横坐标和纵坐标
        val x = event.x.toInt()
        val y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                // 计算移动距离
                val offsetX = x - lastX
                val offsetY = y - lastY
                // 调用layout方法来重新放置它的位置
                layout(
                    left + offsetX,
                    top + offsetY,
                    right + offsetX,
                    bottom + offsetY
                )
                // 或者使用如下方法替代
//                offsetLeftAndRight(offsetX)
//                offsetTopAndBottom(offsetY)
            }
            else -> {
                return true
            }
        }

        return true
    }
}