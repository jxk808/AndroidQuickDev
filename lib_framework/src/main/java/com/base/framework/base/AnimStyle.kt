package com.base.framework.base

import com.base.framework.R


enum class AnimStyle(val resId:Int) {
    DEFAULT(R.style.ScaleAnimStyle),
    SCALE(R.style.ScaleAnimStyle),
    IOS(R.style.IOSAnimStyle),
    TOAST(android.R.style.Animation_Toast),
    TOP(R.style.TopAnimStyle),
    BOTTOM(R.style.BottomAnimStyle),
    LEFT(R.style.LeftAnimStyle),
    RIGHT(R.style.RightAnimStyle)
}