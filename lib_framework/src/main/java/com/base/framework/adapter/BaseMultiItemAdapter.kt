package com.base.framework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.base.framework.interfaces.MultiItemEntity

/**
 * @author HhHh
 * @date   2023/6/27
 * @desc   多种类目Adapter
 */
@Suppress("unused")
abstract class BaseMultiItemAdapter<T : MultiItemEntity> : BaseRecyclerViewAdapter<T, ViewBinding>() {

    /**
     * model需要实现MultiItemEntity接口
     */
    override fun getDefItemViewType(position: Int): Int {
        return getData()[position].itemType
    }

    /**
     * 如果需要实现多种类型子类ViewHolder，可以重写该方法
     */
    override fun onCreateDefViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return BaseBindViewHolder(getViewBinding(layoutInflater, parent, viewType))
    }
}