package cqupt.sl.wanandroidmk

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cqupt.sl.wanandroidmk.home.FragmentHome

class ViewPagerAdapter(fragment: MainActivity, private val count: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FragmentHome()
            1-> FragmentHome()
            else-> Fragment()
        }
    }

}