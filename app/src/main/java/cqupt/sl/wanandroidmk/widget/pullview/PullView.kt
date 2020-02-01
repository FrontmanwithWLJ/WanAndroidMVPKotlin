package cqupt.sl.wanandroidmk.widget.pullview

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout

class PullView : LinearLayout {
    private val TAG = "PullView"
    //记录父控件初始位置
    private val rect: Rect by lazy {
        Rect(left, top, right, bottom)
    }
    //记录是否可以拉动
    private var canPullDown = false
    private var canPullUp = false
    //记录按下的位置
    private var startY = 0f
    //记录按下的事件
    private var downEvent:MotionEvent?=null
    //记录是否移动了布局
    private var isMoved = false
    //阻尼系数
    private var scale = 0.4f
    //弹回动画时间
    private var ANIMATIONTIME = 300L
    //判断是否可以拉动
    private lateinit var callBack: PullCallBack
    //记录滑动状态
    private val DOWN = 1
    private val UP   = 2
    private val NONE = 3
    private var scrollState = NONE
    //记录事件是否交给父控件处理
    private var isParent = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //加载位置
        rect
        //Log.e(TAG,"$left, $top, $right, $bottom")
        super.onLayout(changed, l, t, r, b)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_DOWN->{
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE->{
                val offset = startY - ev.y
                canPullDown = callBack.isCanPullDown()
                canPullUp = callBack.isCanPullUp()
                //Log.e(TAG,"offset=$offset,down = $canPullDown,up = $canPullUp")
                if ((offset < 0 && canPullDown)
                    || (offset > 0 && canPullUp)
                    || (canPullUp && canPullDown)
                ) {
                    setState(offset)
                    isMoved = true
//                    Log.e(TAG,"正向滑动")
                    //移动布局
                    layout(
                        left, (top - offset * scale).toInt(),
                        right, (bottom - offset * scale).toInt()
                    )
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (isMoved){
            if (downEvent!=null) {
                //onTouchEvent(downEvent)
                downEvent = null
            }
            return true
        }
        return false
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.e(TAG, "down")
                downEvent = ev
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                Log.e(TAG, "move")
                val offset = startY - ev.y
                startY = ev.y
                Log.e(TAG, "$offset")
                //Log.e(TAG, "offset = $offset ,down = ${callBack.isCanPullDown()},up = ${callBack.isCanpullUp()},move = $isMoved")
                //处理拉动时反向滑动
//                if (isMoved) {
//                    if (offset > 5 && scrollState == DOWN && bottom - offset*scale  >= rect.bottom) {
//                        Log.e(TAG, "反向滑动1")
//                        layout(
//                            left, (top - offset * scale).toInt(),
//                            right, (bottom - offset * scale).toInt()
//                        )
//                    } else if (offset < -5 && scrollState == UP && top - offset*scale <= rect.top) {
//                        Log.e(TAG, "反向滑动")
//                        layout(
//                            left, (top - offset*scale).toInt(),
//                            right, (bottom - offset*scale).toInt()
//                        )
//                    }
//                }
                canPullDown = callBack.isCanPullDown()
                canPullUp = callBack.isCanPullUp()
                if ((offset < 0 && canPullDown)
                    || (offset > 0 && canPullUp)
                    || (canPullUp && canPullDown)
                ) {
                    setState(offset)
                    isMoved = true
//                    Log.e(TAG,"正向滑动")
                    //移动布局
                    layout(
                        left, (top - offset ).toInt(),
                        right, (bottom - offset ).toInt()
                    )
                }
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_OUTSIDE,
            MotionEvent.ACTION_UP -> {
                Log.e(TAG, "up")
                if (isMoved) {
                    // 开启动画
                    val anim = TranslateAnimation(
                        0F, 0F, top.toFloat(),
                        rect.top.toFloat()
                    )
                    //设置弹回加速度
                    //anim.setInterpolator { input -> 2*input }
                    anim.duration = ANIMATIONTIME
                    startAnimation(anim)
                    // 设置回到正常的布局位置
                    layout(
                        rect.left, rect.top,
                        rect.right, rect.bottom
                    )
                    isMoved = false
//                    Log.e(TAG,"MOVE = $isMoved")
                    startY = 0f
                }
            }
        }
        return true
    }

    private fun setState(offset: Float){
        when {
            offset<0 -> {
                scrollState = DOWN
            }
            offset>0 -> {
                scrollState = UP
            }
            else -> {
                scrollState = NONE
            }
        }
    }

    fun setScale(scale: Float) {
        this.scale = scale
    }

    fun setAnimationTime(time: Long) {
        ANIMATIONTIME = time
    }

    fun setPullCallBack(callBack: PullCallBack) {
        this.callBack = callBack
    }
}