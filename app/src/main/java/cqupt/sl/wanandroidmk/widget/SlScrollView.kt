package cqupt.sl.wanandroidmk.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ScrollView

class SlScrollView : ScrollView {

    private var canPullDown = false
    private var canPullUp = false
    private var isMoved = false
    //唯一子控件
    private lateinit var contentView: View
    //记录唯一子控件的位置
    private lateinit var originRect:Rect
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
        originRect=Rect(contentView.left,contentView.top,contentView.right,contentView.bottom)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        //Log.e("SL","scrolly =$scrollY")
        when(ev!!.action){
            MotionEvent.ACTION_DOWN->{
                startY = ev.y
                canPullDown = isCanPullDown()
                canPullUp = isCanPullUp()
                //Log.e("SL","down=$canPullDown,up=$canPullUp")
            }
            MotionEvent.ACTION_UP->{
                if(!isMoved)return false
                //执行弹回动画
                val reBack = TranslateAnimation(0f,0f
                    ,contentView.top.toFloat(),originRect.top.toFloat())
                reBack.duration = 100
                contentView.startAnimation(reBack)
                //回到原处
                contentView.layout(originRect.left,originRect.top,originRect.right,originRect.bottom)

                canPullDown = false
                canPullUp = false
                isMoved = false
            }
            MotionEvent.ACTION_MOVE->{
                if (!canPullDown&&!canPullUp){
                    startY = ev.y
                    canPullUp = isCanPullUp()
                    canPullDown = isCanPullDown()
                    return false
                }

                val offsetY = ((ev.y-startY)*scale).toInt()
                if ((offsetY>0 && canPullDown)
                    ||(offsetY<0 &&canPullUp)
                    ||(canPullDown&&canPullUp)) {
                    //Log.e("SL","down=$canPullDown,up=$canPullUp,offsetY=${offsetY}")
                    contentView.layout(originRect.left,originRect.top+offsetY
                        ,originRect.right,originRect.bottom+offsetY)
                    isMoved = true
//                    if (offsetY<0)
//                        scale = (contentView.top/originRect.top*2).toDouble()
//                    else if(offsetY>0)
//                        scale = (originRect.top/contentView.top*2).toDouble()
                }
            }
        }
        return false
    }

    private fun isCanPullDown():Boolean{
        return scrollY == 0 ||
                contentView.height < height + scrollY
    }

    private fun isCanPullUp():Boolean{
        return contentView.height <= height + scrollY
    }
}