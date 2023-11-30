package com.base.framework.ext

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

val Context.packageInfo:PackageInfo get() {
    val packageManager = this.packageManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        packageManager.getPackageInfo(this.packageName, PackageManager.PackageInfoFlags.of(0))
    }else{
        packageManager.getPackageInfo(this.packageName,0)
    }
}

val Context.versionCode:Long get() {
    val packageInfo = this.packageInfo

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        packageInfo.versionCode.toLong()
    }
}


val Context.installedPackages:List<PackageInfo> get() {
    val packageManager = this.packageManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
    }else{
        packageManager.getInstalledPackages(0)
    }
}

fun isInstalled(context: Context,packageName:String):Boolean{
    val find = context.installedPackages.find {
        it.packageName == packageName
    }

    return find != null
}

