package cqupt.sl.wanandroidmk.widget

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.ScrollView

class SlScrollView : LinearLayout {

    private var canPullDown = false
    private var canPullUp = false
    private var isMoved = false
    //唯一子控件
    private lateinit var contentView: View
    //记录唯一子控件的位置
    private val originRect = Rect()
    //第一次按下的位置
    private var startY = 0f
    //阻尼系数
    private var scale = 0.4

    constructor(context: Context) : super(context)
    constructor(context: Context,attr: AttributeSet) : super(context,attr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount>0)
            contentView = getChildAt(0)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        originRect.set(contentView.left,contentView.top,contentView.right,contentView.bottom)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_DOWN->{
                startY = ev.y
                canPullDown = isCanPullDown()
                canPullUp = isCanPullUp()
            }
            MotionEvent.ACTION_UP->{
                if(!isMoved)return super.dispatchTouchEvent(ev)
                //执行弹回动画
                val reBack = TranslateAnimation(0f,0f
                    ,contentView.top.toFloat(),originRect.top.toFloat())
                reBack.duration = 100
                contentView.startAnimation(reBack)
                //回到原处
                contentView.layout(originRect.left,originRect.top,originRect.right,originRect.bottom)

                //setOnTouchListener(allowTouch)
                canPullDown = false
                canPullUp = false
                isMoved = false
                //setOnTouchListener{_,_->false}
                //contentView.setOnTouchListener{v,ev->super.onTouchEvent(ev)}
            }
            MotionEvent.ACTION_MOVE-> {
                if (!canPullDown && !canPullUp) {
                    startY = ev.y
                    canPullUp = isCanPullUp()
                    canPullDown = isCanPullDown()
                    return super.dispatchTouchEvent(ev)
                }

                val offsetY = ((ev.y - startY) * scale).toInt()
                if ((offsetY > 0 && canPullDown)
                    || (offsetY < 0 && canPullUp)
                    || (canPullDown && canPullUp)
                ) {
                    contentView.layout(
                        originRect.left, originRect.top + offsetY
                        , originRect.right, originRect.bottom + offsetY
                    )
                    isMoved = true
                    //setOnTouchListener{_,_->true}
//                    contentView.setOnTouchListener { _, _ -> true }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isCanPullDown():Boolean{
        return !canScrollVertically(-1)
    }

    private fun isCanPullUp():Boolean{
        return !canScrollVertically(1)
    }
}