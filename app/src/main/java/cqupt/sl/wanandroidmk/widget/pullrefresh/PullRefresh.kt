package cqupt.sl.wanandroidmk.widget.pullrefresh

import android.content.Context
import android.util.AttributeSet
import com.daimajia.swipe.SwipeLayout

class PullRefresh : SwipeLayout {
    constructor(context:Context):this(context,null)
    constructor(context: Context,attributeSet: AttributeSet?) : this(context,attributeSet,0)
    constructor(context: Context,attributeSet: AttributeSet?,defStyle:Int) : super(context,attributeSet,defStyle)
    private var canScroll:CanScroll?=null

    init {
        //设置跟随滑动
        showMode = ShowMode.PullOut
        addSwipeDenier {
            checkNotNull(canScroll).canUp()
        }
    }

    fun addScrollCallBack(canScroll: CanScroll){
        this.canScroll = canScroll
    }

    fun removeCanScrollCallBack(){
        this.canScroll = null
    }
}