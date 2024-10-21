package com.example.videostoreapplication.notification

import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.videostoreapplication.R

class DownloadForegroundService : Service() {

    private lateinit var downloadManager: DownloadManager
    private lateinit var notificationManager: NotificationManager
    private  val MAX_STORAGE_SIZE = 1 * 1024 * 1024 * 1024 // 1 GB

    override fun onCreate() {
        super.onCreate()
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "download_channel",
                "Video Downloads",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val videoUrl = intent?.getStringExtra("videoUrl")
        videoUrl?.let {
            enqueueVideoDownload(this, it)
        }
        return START_STICKY
    }

    private fun enqueueVideoDownload(context: Context, videoUrl: String) {
        // Manage storage if necessary
        manageVideoStorage(context)

        val videoName = Uri.parse(videoUrl).lastPathSegment ?: "video.mp4"
        val request = DownloadManager.Request(Uri.parse(videoUrl))
            .setTitle("Downloading video")
            .setDescription("Downloading $videoName")
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, videoName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)

        val downloadId = downloadManager.enqueue(request)

        // Start the foreground service with the ongoing "Downloading" notification
        startForeground(1, buildNotification(videoName, "Downloading..."))

        // Track download status
        trackDownloadStatus(context, downloadId, videoName)
    }

    private fun manageVideoStorage(context: Context) {
        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val files = downloadsDir?.listFiles() ?: return

        // Calculate the total size of downloaded videos
        var totalSize = 0L
        val videoFiles = files.filter { it.extension == "mp4" }
        for (file in videoFiles) {
            totalSize += file.length()
        }

        // Delete the oldest video until total size is less than 1GB
        if (totalSize >= MAX_STORAGE_SIZE) {
            videoFiles.sortedBy { it.lastModified() }.forEach { file ->
                if (totalSize >= MAX_STORAGE_SIZE) {
                    totalSize -= file.length()
                    file.delete()
                    Log.d("DeletedOld", "Deleted old video: ${file.name}")
                } else {
                    return
                }
            }
        }
    }

    private fun buildNotification(title: String, content: String): Notification {
        return NotificationCompat.Builder(this, "download_channel")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setProgress(0, 0, true)
            .build()
    }

    private fun trackDownloadStatus(context: Context, downloadId: Long, videoName: String) {
        // Use BroadcastReceiver or a similar mechanism to track the download status
        // On download completion, call stopForeground() to stop the service
    }

    private fun onDownloadComplete(videoName: String) {
        // Notify user that the download has completed
        val notification = NotificationCompat.Builder(this, "download_channel")
            .setContentTitle(videoName)
            .setContentText("Download complete")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        // Update the notification
        notificationManager.notify(1, notification)

        // Stop the foreground service and remove the ongoing notification
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up resources if necessary
    }
}