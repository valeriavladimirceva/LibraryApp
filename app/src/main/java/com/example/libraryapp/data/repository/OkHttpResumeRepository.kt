package com.example.libraryapp.data.repository

import android.content.Context
import androidx.core.content.FileProvider
import com.example.libraryapp.domain.model.DomainError
import com.example.libraryapp.domain.repository.ResumeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class OkHttpResumeRepository(
    private val context: Context,
    private val client: OkHttpClient
) : ResumeRepository {

    override suspend fun download(url: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                when {
                    response.code == 404 -> Result.failure(DomainError.NotFound)
                    !response.isSuccessful -> Result.failure(DomainError.Server)
                    else -> {
                        val input = response.body?.byteStream()
                            ?: return@use Result.failure(DomainError.Unknown)
                        val dir = File(context.filesDir, "resume").apply { mkdirs() }
                        val file = File(dir, "resume.pdf")
                        input.use { source ->
                            file.outputStream().use { target -> source.copyTo(target) }
                        }
                        Result.success(
                            FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                file
                            ).toString()
                        )
                    }
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Result.failure(DomainError.NoConnection)
        } catch (e: Exception) {
            Result.failure(DomainError.Unknown)
        }
    }
}