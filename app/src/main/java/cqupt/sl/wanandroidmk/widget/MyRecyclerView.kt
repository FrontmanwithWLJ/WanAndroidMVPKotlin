package cqupt.sl.wanandroidmk.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerView:RecyclerView {
    private var canPullDown = false
    private var canPullUp = false
    private var isMoved = false
    //记录自身初始的位置
    private val originRect = Rect()
    //第一次按下的位置
    private var startY = 0f
    //阻尼系数
    private var scale = 0.4

    constructor(context: Context) : super(context)
    constructor(context: Context,attr: AttributeSet) : super(context,attr)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        originRect.set(this.left,this.top,this.right,this.bottom)
        Log.e("SL","onLayout originRect.left = ${originRect.left},top=${originRect.top},right=${originRect.right},bottom=${originRect.bottom}")
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_DOWN->{
                Log.e("SL","down")
                startY = ev.y
                canPullDown = isCanPullDown()
                canPullUp = isCanPullUp()
            }
            MotionEvent.ACTION_UP->{
                Log.e("SL","up")
                if(!isMoved)return super.dispatchTouchEvent(ev)
                Log.e("SL","back")
                //执行弹回动画
                val reBack = TranslateAnimation(0f,0f
                    ,this.top.toFloat(),originRect.top.toFloat())
                reBack.duration = 100
                this.startAnimation(reBack)
                //回到原处
                Log.e("SL","this.left = ${this.left},top=${this.top},right=${this.right},bottom=${this.bottom}")
                this.layout(originRect.left,originRect.top,originRect.right,originRect.bottom)
                Log.e("SL","originRect.left = ${originRect.left},top=${originRect.top},right=${originRect.right},bottom=${originRect.bottom}")
                canPullDown = false
                canPullUp = false
                isMoved = false
                //isNestedScrollingEnabled = true
            }
            MotionEvent.ACTION_MOVE->{
                Log.e("SL","move")
                if (!canPullDown&&!canPullUp){
                    startY = ev.y
                    canPullUp = isCanPullUp()
                    canPullDown = isCanPullDown()
                    return super.dispatchTouchEvent(ev)
                }

                val offsetY = ((ev.y-startY)*scale).toInt()
                if ((offsetY>0 && canPullDown)
                    ||(offsetY<0 &&canPullUp)
                    ||(canPullDown&&canPullUp)
                    ||isMoved) {
                    this.layout(originRect.left,originRect.top+offsetY
                        ,originRect.right,originRect.bottom+offsetY)
                    isMoved = true
                    //isNestedScrollingEnabled = false
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    //判断是否滑动到底部， recyclerView.canScrollVertically(1);返回false表示不能往上滑动，即代表到底部了；
    //判断是否滑动到顶部， recyclerView.canScrollVertically(-1);返回false表示不能往下滑动，即代表到顶部了；
    private fun isCanPullDown():Boolean{
        return !canScrollVertically(-1)
    }

    private fun isCanPullUp():Boolean{
        return !canScrollVertically(1)
    }
}