package com.base.framework.adapter

import android.util.SparseArray
import androidx.core.util.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @创建者 HhHh
 * @创建时间 2023/3/5 15:32
 * @类描述 FragmentStateAdapter
 */
class ViewPage2FragmentAdapter(activity: FragmentActivity, var fragments: SparseArray<Fragment>) :
    FragmentStateAdapter(activity) {
    //FragmentStateAdapter内部自己会管理已实例化的fragment对象，所以不需要考虑复用的问题。
    override fun createFragment(i: Int): Fragment {
        return fragments[i]
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    fun setData(fragments: SparseArray<Fragment>) {
        this.fragments = fragments
    }
}
