package com.base.framework.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author HhHh
 * @date   2023/6/27
 * @desc   基本ViewHolder
 */
open class BaseViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

open class BaseBindViewHolder<B : ViewBinding>(val binding: B) : BaseViewHolder(binding.root)