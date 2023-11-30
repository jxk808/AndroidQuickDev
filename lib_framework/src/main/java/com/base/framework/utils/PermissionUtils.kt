package com.base.framework.utils

import android.Manifest
import android.content.Context
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

class PermissionUtils(private val mContext: Context) {

    fun permissionIsGranted(permission:String):Boolean{
        return XXPermissions.isGranted(mContext,permission)
    }

    fun requestPermission(onGranted: (allGranted: Boolean) -> Unit, onDenied: (quick: Boolean) -> Unit,permission: String){
        XXPermissions.with(mContext)
            .permission(permission)
            .request(object: OnPermissionCallback{
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        onGranted(false)
                        return
                    }else{
                        onGranted(true)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    //该条件不可靠，详细参考:https://github.com/getActivity/XXPermissions/issues/154
                    if (doNotAskAgain) {
                        XXPermissions.startPermissionActivity(mContext, permissions)
                        onDenied(true)
                    } else {
                        onDenied(false)
                    }
                }
            })
    }

    fun requestFilePerMission(onGranted: (allGranted: Boolean) -> Unit, onDenied: (quick: Boolean) -> Unit){
        XXPermissions.with(mContext)
            .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                    if (!all) {
                        onGranted(false)
                        return
                    }else{
                        onGranted(true)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                    if (never) {
                        XXPermissions.startPermissionActivity(mContext, permissions)
                        onDenied(true)
                    } else {
                        onDenied(false)
                    }
                }
            })

    }

    fun requestCameraPermission(onGranted: (allGranted: Boolean) -> Unit, onDenied: (quick: Boolean) -> Unit) {
        XXPermissions.with(mContext)
            .permission(Permission.CAMERA)
            .request(object: OnPermissionCallback{
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        onGranted(false)
                        return
                    }else{
                        onGranted(true)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    //该条件不可靠，详细参考:https://github.com/getActivity/XXPermissions/issues/154
                    if (doNotAskAgain) {
                        XXPermissions.startPermissionActivity(mContext, permissions)
                        onDenied(true)
                    } else {
                        onDenied(false)
                    }
                }
            })
    }

    fun requestLocationPermission(onGranted: (allGranted: Boolean) -> Unit, onDenied: (quick: Boolean) -> Unit) {
        XXPermissions.with(mContext)
            .permission(Permission.ACCESS_FINE_LOCATION)
            .request(object: OnPermissionCallback{
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        onGranted(false)
                        return
                    }else{
                        onGranted(true)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    //该条件不可靠，详细参考:https://github.com/getActivity/XXPermissions/issues/154
                    if (doNotAskAgain) {
                        XXPermissions.startPermissionActivity(mContext, permissions)
                        onDenied(true)
                    } else {
                        onDenied(false)
                    }
                }
            })
    }

    fun requestMicroPermission(onGranted: (allGranted: Boolean) -> Unit, onDenied: (quick: Boolean) -> Unit) {
        XXPermissions.with(mContext)
            .permission(Permission.RECORD_AUDIO)
            .request(object: OnPermissionCallback{
                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    if (!allGranted) {
                        onGranted(false)
                        return
                    }else{
                        onGranted(true)
                    }
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    //该条件不可靠，详细参考:https://github.com/getActivity/XXPermissions/issues/154
                    if (doNotAskAgain) {
                        XXPermissions.startPermissionActivity(mContext, permissions)
                        onDenied(true)
                    } else {
                        onDenied(false)
                    }
                }
            })
    }

}