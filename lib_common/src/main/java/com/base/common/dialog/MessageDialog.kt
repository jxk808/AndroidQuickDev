package com.base.common.dialog

import android.app.Dialog
import android.text.method.LinkMovementMethod
import com.base.framework.base.BaseDialog.AnimStyle.TOAST
import com.base.framework.base.BaseDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentActivity
import com.sum.common.databinding.DialogMessageBinding
import com.base.framework.ext.onClick
import com.base.framework.manager.AppManager

/**
 * 普通弹框
 */
@Suppress("ControlFlowWithEmptyBody", "unused", "MemberVisibilityCanBePrivate", "UNUSED_PARAMETER")
class MessageDialog {

    class Builder(
        context: FragmentActivity
    ) : BaseDialog.Builder<Builder>(context) {
        private val mBinding: DialogMessageBinding = DialogMessageBinding.inflate(LayoutInflater.from(context))
        private var notShowAgain = false //仅弹出一次
        private var tag = "" //储存不再提示状态的tag
        private var mAutoDismiss = true // 设置点击按钮后自动消失
        private var onConfirm: ((Dialog?) -> Unit)? = null

        init {
            setContentView(mBinding.root)
            //  Log.d("屏幕宽带:","${(AppManager.getScreenWidthPx() * 0.8).toInt()}")
            setWidth((AppManager.getScreenWidthPx(context) * 0.8).toInt())
            setAnimStyle(TOAST)
            setGravity(Gravity.CENTER)
        }

        override fun show(): BaseDialog? {
            if (tag.isNotEmpty()){
              //  notShowAgain = MMKV.defaultMMKV().decodeBool(tag,false)
            }

            return if(!notShowAgain){
                super.show()
            } else{
                onConfirm?.invoke(dialog)
                null
            }

        }

        fun setTitle(resId: Int): Builder {
            return setTitle(getText(resId))
        }

        fun setTitle(text: CharSequence?): Builder {
            mBinding.tvDialogMessageTitle.text = text
            return this
        }

        fun setMessage(resId: Int): Builder {
            return setMessage(getText(resId))
        }

        fun setMessage(text: CharSequence?): Builder {
            mBinding.tvDialogMessageMessage.text = text
            return this
        }

        fun setMessageTextSize(size: Float): Builder {
            mBinding.tvDialogMessageMessage.textSize = size
            return this
        }

        fun setMessageCenter(): Builder {
            mBinding.tvDialogMessageMessage.gravity = Gravity.CENTER
            return this
        }

        fun setMessageTxtColor(@ColorInt color: Int): Builder {
            mBinding.tvDialogMessageMessage.setTextColor(color)
            return this
        }

        fun setMessageTextMovement(){
            mBinding.tvDialogMessageMessage.movementMethod = LinkMovementMethod.getInstance()
        }

        fun setCancel(resId: Int): Builder {
            return setCancel(getText(resId))
        }

        fun setCancel(text: CharSequence?): Builder {
            mBinding.tvDialogMessageCancel.text = text
            val isEmpty = text.isNullOrEmpty()
            mBinding.tvDialogMessageCancel.visibility = if (isEmpty) View.GONE else View.VISIBLE
//            mBinding.tvDialogMessageConfirm.setBackgroundResource(if (isEmpty) R.drawable.dialog_message_one_button else R.drawable.dialog_message_right_button)
            return this
        }

        fun setConfirm(resId: Int): Builder {
            return setConfirm(getText(resId))
        }

        fun setConfirm(text: CharSequence?): Builder {
            mBinding.tvDialogMessageConfirm.text = text
            return this
        }

        fun setConfirmTxtColor(@ColorInt color: Int): Builder {
            mBinding.tvDialogMessageConfirm.setTextColor(color)
            return this
        }

        fun setCancelTextColor(@ColorInt color: Int): Builder {
            mBinding.tvDialogMessageCancel.setTextColor(color)
            return this
        }

        fun setAutoDismiss(dismiss: Boolean): Builder {
            mAutoDismiss = dismiss
            return this
        }

        fun setNoMoreAsk(tag: String,defaultBoolean: Boolean = false){
            if (tag.isNotEmpty()){
                this.tag = tag
             //   mBinding.noMoreShowCheck.isChecked = defaultBoolean
            }
        }

        fun setonConfirmListener(onConfirm: (dialog: Dialog?) -> Unit): Builder {
            this.onConfirm = onConfirm
            mBinding.tvDialogMessageConfirm.onClick {
                onConfirm.invoke(dialog)
                if (tag.isNotEmpty()){
                  //  MMKV.defaultMMKV().encode(tag,mBinding.noMoreShowCheck.isChecked)
                }
            }
            return this
        }

        fun setonCancelListener(onCancel: (dialog: Dialog?) -> Unit): Builder {
            mBinding.tvDialogMessageCancel.onClick {
                onCancel.invoke(dialog)
                if (tag.isNotEmpty()){
                  //  MMKV.defaultMMKV().encode(tag,mBinding.noMoreShowCheck.isChecked)
                }
            }
            return this
        }

        fun setDoNotShowAgain(tag:String,show:Boolean = false){
            this.tag = tag
            if (tag.isNotEmpty()){
                notShowAgain = show
            //    mBinding.noMoreShowCheck.visibility = View.VISIBLE
            }
        }

        override fun create(): BaseDialog {
            // 如果标题为空就隐藏
            if (mBinding.tvDialogMessageTitle.text.isNullOrEmpty()) {
                mBinding.tvDialogMessageTitle.visibility = View.GONE
            }
            // 如果内容为空就抛出异常
//           if (TextUtils.isEmpty(mMessageView.getText())) {
//                //throw new IllegalArgumentException("Dialog message not null");
//           }
            return super.create()
        }
    }

}