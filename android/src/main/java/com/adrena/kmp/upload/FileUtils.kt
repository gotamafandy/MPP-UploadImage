package com.adrena.kmp.upload

import android.content.Context
import android.os.Environment
import java.io.File
import java.util.*

class FileUtils {
    companion object
}

fun FileUtils.Companion.createFile(context: Context, directory: String? = null): File? {
    val imageName = "${UUID.randomUUID()}.jpg"

    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    val path: String?

    if (storageDir != null) {
        path = if (directory != null) {
            storageDir.absolutePath + File.separator + directory
        } else {
            storageDir.absolutePath
        }
    } else {
        path = null
    }

    if (path != null) {
        val filePath = File(path)

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val pathWithDirName = File(path, imageName)

        if (!pathWithDirName.exists()) {
            pathWithDirName.createNewFile()
        }

        return pathWithDirName
    }

    return null
}

fun FileUtils.Companion.deleteDir(dir: File): Boolean {
    if (dir.isDirectory) {
        val children = dir.list()
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
    }
    // The directory is now empty so delete it
    return dir.delete()
}