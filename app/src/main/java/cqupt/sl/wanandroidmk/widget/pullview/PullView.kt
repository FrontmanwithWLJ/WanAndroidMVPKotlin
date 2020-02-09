package cqupt.sl.wanandroidmk.widget.pullview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import java.util.concurrent.locks.ReentrantLock

class PullView : LinearLayout {
    private val TAG = "PullView"
    //子控件实例
    private lateinit var contentView: View
    //记录子控件初始位置
    private lateinit var rect: Rect
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
    //动画锁
    private val lock = ReentrantLock()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private fun lockLayout(l:Int, t:Int, r:Int, b:Int){
        if (lock.isLocked)
            return
        contentView.layout(l,t,r,b)
    }

    override fun onFinishInflate() {
        //获取子控件和加载位置
        if (childCount>0){
            contentView = getChildAt(0)
        }
        super.onFinishInflate()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        rect = Rect(contentView.left, contentView.top, contentView.right, contentView.bottom)
        super.onLayout(changed, l, t, r, b)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                //Log.e(TAG, "down")
                startY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                //Log.e(TAG, "move")
                if (startY==0f)
                    startY = ev.y
                val offset = startY - ev.y
                startY = ev.y
                //处理拉动时反向滑动
                if (isMoved) {
                    if (offset > 5 && scrollState == DOWN && bottom - offset*scale  >= rect.bottom) {
                        //Log.e(TAG, "反向滑动1")
                        lockLayout(contentView.left, (contentView.top - offset * scale).toInt(),
                            contentView.right, (contentView.bottom - offset * scale).toInt())
                    } else if (offset < -5 && scrollState == UP && top - offset*scale <= rect.top) {
                        //Log.e(TAG, "反向滑动")
                        lockLayout(contentView.left, (contentView.top - offset * scale).toInt(),
                            contentView.right, (contentView.bottom - offset * scale).toInt())
                    }
                }
                canPullDown = callBack.isCanPullDown()
                canPullUp = callBack.isCanPullUp()
                if ((offset < 0 && canPullDown)
                    || (offset > 0 && canPullUp)
                    || (canPullUp && canPullDown)) {
                    setState(offset)
                    isMoved = true
//                    Log.e(TAG,"正向滑动")
                    //移动布局
                    lockLayout(contentView.left, (contentView.top - offset * scale).toInt(),
                        contentView.right, (contentView.bottom - offset * scale).toInt())
                }
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_OUTSIDE,
            MotionEvent.ACTION_UP -> {
                //Log.e(TAG, "up")
                if (isMoved) {
                    // 开启回弹动画
                    backAnim()
                    isMoved = false
                    startY = 0f
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        //Log.e(TAG,"拦截 $isMoved")
        if (isMoved){
            if (isParent) {
                isParent = false
                return onTouchEvent(ev.apply { this!!.action = MotionEvent.ACTION_DOWN })
            }
            return true
        }
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    private fun backAnim(){
        //Log.e(TAG,"top = ${contentView.top},${rect.top}")
        val anim = TranslateAnimation(
            0F, 0F, contentView.top.toFloat(), rect.top.toFloat())
        //设置弹回加速度
        //anim.setInterpolator { input ->  }
        anim.duration = ANIMATIONTIME
        anim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                lock.unlock()
            }

            override fun onAnimationStart(animation: Animation?) {
                lock.lock()
                contentView.layout(rect.left, rect.top,
                    rect.right, rect.bottom)
            }
        })
        contentView.startAnimation(anim)
    }

    private fun setState(offset: Float){
        scrollState = when {
            offset<0 -> DOWN
            offset>0 -> UP
            else -> NONE
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