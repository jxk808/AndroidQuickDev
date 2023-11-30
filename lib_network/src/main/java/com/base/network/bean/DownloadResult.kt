package com.base.network.bean

import kotlin.Exception

sealed class DownloadResult{
    class Error(val exception: Exception): DownloadResult()
    class Success(val outPath:String): DownloadResult()
}

class DownloadUrlError:Exception()
