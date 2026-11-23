package com.example.libraryapp.profile.data

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.libraryapp.core.DomainError
import com.example.libraryapp.profile.domain.repository.ResumeRepository
import kotlinx.coroutines.delay
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

class DownloadManagerResumeRepository(context: Context) : ResumeRepository {

    private val context = context.applicationContext
    private val downloadManager = this.context.getSystemService(DownloadManager::class.java)

    override suspend fun download(url: String): Result<String> {
        val request = try {
            buildRequest(url)
        } catch (e: IllegalArgumentException) {
            return Result.failure(DomainError.Unknown)
        }

        val id = downloadManager.enqueue(request)
        return try {
            awaitOutcome(id)
        } catch (e: CancellationException) {
            downloadManager.remove(id)
            throw e
        }
    }

    private fun buildRequest(url: String): DownloadManager.Request {
        File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), FILE_NAME).delete()

        return DownloadManager.Request(Uri.parse(url))
            .setTitle(FILE_NAME)
            .setMimeType(MIME_PDF)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, FILE_NAME)
    }

    private suspend fun awaitOutcome(id: Long): Result<String> {
        while (true) {
            queryOutcome(id)?.let { return it }
            delay(POLL_INTERVAL_MS)
        }
    }

    private fun queryOutcome(id: Long): Result<String>? {
        val cursor = downloadManager.query(DownloadManager.Query().setFilterById(id))
            ?: return Result.failure(DomainError.Unknown)

        return cursor.use {
            if (!it.moveToFirst()) return@use Result.failure(DomainError.Unknown)

            val status = it.getInt(it.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            val reason = it.getInt(it.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON))

            when (status) {
                DownloadManager.STATUS_SUCCESSFUL ->
                    downloadManager.getUriForDownloadedFile(id)
                        ?.let { uri -> Result.success(uri.toString()) }
                        ?: Result.failure(DomainError.Unknown)

                DownloadManager.STATUS_FAILED -> {
                    downloadManager.remove(id)
                    Result.failure(failureToError(reason))
                }

                DownloadManager.STATUS_PAUSED ->
                    if (reason == DownloadManager.PAUSED_WAITING_FOR_NETWORK ||
                        reason == DownloadManager.PAUSED_WAITING_TO_RETRY
                    ) {
                        downloadManager.remove(id)
                        Result.failure(DomainError.NoConnection)
                    } else null

                else -> null
            }
        }
    }

    private fun failureToError(reason: Int): DomainError = when (reason) {
        404 -> DomainError.NotFound
        in 400..599 -> DomainError.Server
        DownloadManager.ERROR_HTTP_DATA_ERROR -> DomainError.NoConnection
        DownloadManager.ERROR_UNHANDLED_HTTP_CODE,
        DownloadManager.ERROR_TOO_MANY_REDIRECTS -> DomainError.Server
        else -> DomainError.Unknown
    }

    private companion object {
        const val FILE_NAME = "resume.pdf"
        const val MIME_PDF = "application/pdf"
        const val POLL_INTERVAL_MS = 500L
    }
}