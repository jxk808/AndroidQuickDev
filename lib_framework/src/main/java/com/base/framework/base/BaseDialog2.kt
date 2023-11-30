package com.base.framework.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.base.framework.R
import java.lang.reflect.ParameterizedType

open class BaseDialog2<T : ViewBinding> : DialogFragment() {
    private var _binding: T? = null
    protected val bind get() = _binding!!

    var animations = AnimStyle.TOAST

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BaseDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.attributes?.apply {

          /*      when (this.gravity) {
                    Gravity.TOP -> animations = BaseDialog.AnimStyle.TOP
                    Gravity.BOTTOM -> animations = BaseDialog.AnimStyle.BOTTOM
                    Gravity.LEFT -> animations = BaseDialog.AnimStyle.LEFT
                    Gravity.RIGHT -> animations = BaseDialog.AnimStyle.RIGHT
                    else -> {}
                }*/

                windowAnimations = animations.resId
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun initContentBinding(inflater: LayoutInflater, container: ViewGroup?): T? {
        val type = javaClass.genericSuperclass as ParameterizedType
        val anyClass = type.actualTypeArguments[0] as Class<*>
        val method = anyClass.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
        return method.invoke(null, inflater, container, false) as? T
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = initContentBinding(inflater, container)
        return bind.root
    }

    var dismissed:(()->Unit)? = null

    override fun onDismiss(dialog: DialogInterface) {
        dismissed?.invoke()

        super.onDismiss(dialog)
    }
}