package com.base.framework.utils

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import com.base.framework.R
import java.util.*

object IconsUtil {
    private const val ALL_MIME_TYPES = "*/*"
    private val MIME_TYPES = HashMap<String, String>()
    private val sMimeIconIds = HashMap<String, Int>()
    const val NOT_KNOWN = -1
    const val APK = 0
    const val AUDIO = 1
    const val CERTIFICATE = 2
    const val CODE = 3
    const val COMPRESSED = 4
    const val CONTACT = 5
    const val EVENTS = 6
    const val FONT = 7
    const val IMAGE = 8
    const val PDF = 9
    const val PRESENTATION = 10
    const val SPREADSHEETS = 11
    const val DOCUMENTS = 12
    const val TEXT = 13
    const val VIDEO = 14
    const val ENCRYPTED = 15
    const val GIF = 16

    init {
        putKeys(APK, "application/vnd.android.package-archive")
        putKeys(AUDIO, "application/ogg", "application/x-flac")
        putKeys(CERTIFICATE, "application/pgp-keys", "application/pgp-signature", "application/x-pkcs12", "application/x-pkcs7-certreqresp", "application/x-pkcs7-crl", "application/x-x509-ca-cert", "application/x-x509-user-cert", "application/x-pkcs7-certificates", "application/x-pkcs7-mime", "application/x-pkcs7-signature")
        putKeys(CODE, "application/rdf+xml", "application/rss+xml", "application/x-object", "application/xhtml+xml", "text/css", "text/html", "text/xml", "text/x-c++hdr", "text/x-c++src", "text/x-chdr", "text/x-csrc", "text/x-dsrc", "text/x-csh", "text/x-haskell", "text/x-java", "text/x-literate-haskell", "text/x-pascal", "text/x-tcl", "text/x-tex", "application/x-latex", "application/x-texinfo", "application/atom+xml", "application/ecmascript", "application/json", "application/javascript", "application/xml", "text/javascript", "application/x-javascript")
        putKeys(COMPRESSED, "application/mac-binhex40", "application/rar", "application/zip", "application/gzip", "application/java-archive", "application/x-apple-diskimage", "application/x-debian-package", "application/x-gtar", "application/x-iso9660-image", "application/x-lha", "application/x-lzh", "application/x-lzx", "application/x-stuffit", "application/x-tar", "application/x-webarchive", "application/x-webarchive-xml", "application/x-gzip", "application/x-7z-compressed", "application/x-deb", "application/x-rar-compressed", "application/x-lzma", "application/x-xz", "application/x-bzip2")
        putKeys(CONTACT, "text/x-vcard", "text/vcard")
        putKeys(EVENTS, "text/calendar", "text/x-vcalendar")
        putKeys(FONT, "application/x-font", "application/font-woff", "application/x-font-woff", "application/x-font-ttf")
        putKeys(IMAGE, "application/vnd.oasis.opendocument.graphics", "application/vnd.oasis.opendocument.graphics-template", "application/vnd.oasis.opendocument.image", "application/vnd.stardivision.draw", "application/vnd.sun.xml.draw", "application/vnd.sun.xml.draw.template", "image/jpeg", "image/png")
        putKeys(PDF, "application/pdf")
        putKeys(PRESENTATION, "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/vnd.openxmlformats-officedocument.presentationml.template", "application/vnd.openxmlformats-officedocument.presentationml.slideshow", "application/vnd.stardivision.impress", "application/vnd.sun.xml.impress", "application/vnd.sun.xml.impress.template", "application/x-kpresenter", "application/vnd.oasis.opendocument.presentation")
        putKeys(SPREADSHEETS, "application/vnd.oasis.opendocument.spreadsheet", "application/vnd.oasis.opendocument.spreadsheet-template", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.openxmlformats-officedocument.spreadsheetml.template", "application/vnd.stardivision.calc", "application/vnd.sun.xml.calc", "application/vnd.sun.xml.calc.template", "application/x-kspread", "text/comma-separated-values")
        putKeys(DOCUMENTS, "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.openxmlformats-officedocument.wordprocessingml.template", "application/vnd.oasis.opendocument.text", "application/vnd.oasis.opendocument.text-master", "application/vnd.oasis.opendocument.text-template", "application/vnd.oasis.opendocument.text-web", "application/vnd.stardivision.writer", "application/vnd.stardivision.writer-global", "application/vnd.sun.xml.writer", "application/vnd.sun.xml.writer.global", "application/vnd.sun.xml.writer.template", "application/x-abiword", "application/x-kword", "text/markdown")
        putKeys(TEXT, "text/plain")
        putKeys(VIDEO, "application/x-quicktimeplayer", "application/x-shockwave-flash")
        putKeys(ENCRYPTED, "application/octet-stream")
    }


    private fun put(mimeType: String, resId: Int) {
        sMimeIconIds[mimeType] = resId
    }

    private fun putKeys(resId: Int, vararg mimeTypes: String) {
        for (type in mimeTypes) {
            put(type, resId)
        }
    }

//    fun loadMimeLogo(path: String, isDirectory: Boolean): Int {
//        when {
//            path == ".." -> return R.drawable.ic_arrow_left_white_24dp
//            getTypeOfFile(path) == APK -> return R.mipmap.apk
//            getTypeOfFile(path) == AUDIO -> return R.mipmap.pm3
//            getTypeOfFile(path) == IMAGE -> return R.mipmap.photo
//            getTypeOfFile(path) == TEXT -> return R.mipmap.text
//            getTypeOfFile(path) == VIDEO -> return R.mipmap.pm4
//            isDirectory -> return R.mipmap.ic_file
//            else -> return R.mipmap.ic_unknown_file
//        }
//    }

    private fun getTypeOfFile(path: String): Int {
        val mimeType = getMimeType(path)
        Log.d("FileType2", sMimeIconIds[mimeType].toString() + "")
        return sMimeIconIds[mimeType] ?: when {
            mimeType.startsWith("text") -> TEXT
            mimeType.startsWith("image") -> IMAGE
            mimeType.startsWith("video") -> VIDEO
            mimeType.startsWith("audio") -> AUDIO
            mimeType.startsWith("crypt") -> ENCRYPTED
            else -> NOT_KNOWN
        }
    }

    private fun getMimeType(path: String?): String {
//        if (isDirectory) {
//            return null;
//        }
        Log.d("FileType4", path.toString() + "")
        var type: String = ALL_MIME_TYPES
        val extension: String = getExtension(path?:"")

        // mapping extension to system mime types
        if (extension.isNotEmpty()) {
            val extensionLowerCase = extension.lowercase(Locale.getDefault())
            val mime = MimeTypeMap.getSingleton()
            type = mime.getMimeTypeFromExtension(extensionLowerCase).toString()
            Log.d("FileType3", type.toString() + "")
        }
        return type
    }

    fun getExtension(path: String): String {
        return if (path.contains(".")) {
            path.substring(path.lastIndexOf(".") + 1).toLowerCase(Locale.getDefault())
        } else ""
    }

    @SuppressLint("DefaultLocale")
    fun getApplicationName(mContext: AppCompatActivity, applicationInfo: ApplicationInfo): String {
        val packageManager = mContext.packageManager
        return packageManager.getApplicationLabel(applicationInfo).toString()
    }
}
