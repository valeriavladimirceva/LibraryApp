package com.example.libraryapp.data.local

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.libraryapp.domain.repository.PhotoStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class FilePhotoStorage(private val context: Context) : PhotoStorage {

    private fun newAvatarFile(): File {
        val dir = File(context.filesDir, "avatars").apply { mkdirs() }
        return File(dir, "avatar_${System.currentTimeMillis()}.jpg")
    }

    private fun File.toContentUri(): String =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", this).toString()

    override fun createCameraTarget(): String {
        val file = newAvatarFile().also { it.createNewFile() }
        return file.toContentUri()
    }

    override suspend fun importPhoto(sourceUri: String): String = withContext(Dispatchers.IO) {
        val file = newAvatarFile()
        val input = context.contentResolver.openInputStream(Uri.parse(sourceUri))
            ?: throw IOException("Cannot open $sourceUri")
        input.use { source ->
            file.outputStream().use { target -> source.copyTo(target) }
        }
        file.toContentUri()
    }
}