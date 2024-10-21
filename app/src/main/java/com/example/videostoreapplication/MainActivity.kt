package com.example.videostoreapplication

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.casino.retrofit.BaseResponse
import com.example.videostoreapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var allHomePostViewModel: AllHomePostViewModel

    lateinit var binding: ActivityMainBinding
    var totalCount = 30
    var last_page = 10
    var page = 0
    var currentPage = 0
    var TAG= "MainActityLog"
    private  val MAX_STORAGE_SIZE = 2 * 1024 * 1024 * 1024 // 2 GB
    private val videoList = mutableListOf<String>()
    private lateinit var videoAdapter: HomePostVideoAdapter
    private var isLoading = false // To prevent multiple simultaneous API calls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvPost.layoutManager = LinearLayoutManager(this)
        getHomePostApiCall()
        setupRecyclerView()
    }



    private fun getHomePostApiCall() {
        allHomePostViewModel = ViewModelProvider(this)[AllHomePostViewModel::class.java]

        if (isLoading) return // Prevent loading if already in progress

        isLoading = true // Set loading to true to prevent multiple calls

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val currentPageToLoad = currentPage + 1 // Increment the page to load the next one
                allHomePostViewModel.getHomePost("0", currentPageToLoad, "")

                allHomePostViewModel.getPostResponse.observe(this@MainActivity) {
                    when (it) {
                        is BaseResponse.Loading -> {
                            binding.pbProgress.visibility = View.VISIBLE
                        }
                        is BaseResponse.Success -> {
                            binding.pbProgress.visibility = View.GONE
                            if (it.data!!.statusCode == 200) {
                                if (it.data.data.isNotEmpty()) {
                                    binding.txtNoData.visibility = View.GONE
                                    last_page = it.data.meta.lastPage!!
                                    currentPage = it.data.meta.currentPage!!

                                    val videoList = mutableListOf<String>()
                                    it.data.data.forEach { post ->
                                        post.post_images.filter { image -> image.type == "video" }
                                            .forEach { video ->
                                                video.item_name?.let { videoUrl ->
                                                    videoList.add(videoUrl)
                                                    enqueueVideoDownload(this@MainActivity, videoUrl) // Download the video
                                                }
                                            }
                                    }

                                    videoAdapter.addVideos(videoList) // Add new videos to the adapter

                                    isLoading = false // Reset loading state after processing
                                } else {
                                    isLoading = false // Reset loading state if no data is received
                                    binding.txtNoData.visibility = View.VISIBLE
                                    binding.rvPost.visibility = View.INVISIBLE
                                }
                            } else {
                                Toast.makeText(this@MainActivity, it.data.message, Toast.LENGTH_SHORT).show()
                                isLoading = false // Reset loading state on error
                            }
                        }
                        is BaseResponse.Error -> {
                            binding.pbProgress.visibility = View.GONE
                            Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                            isLoading = false // Reset loading state on error
                        }
                        else -> {
                            binding.pbProgress.visibility = View.GONE
                            isLoading = false // Reset loading state if any other response
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "initViews: home postlist _catcha " + e.message)
                isLoading = false // Ensure loading state is reset on exception
            }
        }
    }


    private fun setupRecyclerView() {
        videoAdapter = HomePostVideoAdapter(mutableListOf(), { videoUri ->
            lifecycleScope.launch(Dispatchers.Main) {
                playVideoOffline(videoUri) // Play video when clicked
            }
        }, binding.rvPost)

        binding.rvPost.layoutManager = LinearLayoutManager(this)
        binding.rvPost.adapter = videoAdapter

        binding.rvPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.rvPost.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // Load more videos if the last item is visible
                if (lastVisibleItemPosition == totalItemCount - 1 && !isLoading && currentPage < last_page) {
                    getHomePostApiCall() // Fetch more videos
                }

                // Handle video playback logic here
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                for (i in firstVisibleItemPosition..lastVisibleItemPosition) {
                    val holder = binding.rvPost.findViewHolderForAdapterPosition(i) as? HomePostVideoAdapter.VideoViewHolder
                    if (holder != null) {
                        // If the video is fully visible, play it
                        if (holder.itemView.isShown) {
                            videoAdapter.playVideoAt(i)
                        } else {
                            // Pause if it's not fully visible
                            holder.pauseVideo()
                        }
                    }
                }
            }
        })
    }



    private fun enqueueVideoDownload(context: Context, videoUrl: String) {
        // Delete old videos if total size exceeds 1GB
        manageVideoStorage(context)

        val videoName = Uri.parse(videoUrl).lastPathSegment ?: "video.mp4"
        val request = DownloadManager.Request(Uri.parse(videoUrl))
            .setTitle("Downloading video")
            .setDescription("Downloading $videoName")
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, videoName)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = downloadManager.enqueue(request)

        // Track download status
        trackDownloadStatus(context, downloadId, videoName)
    }

    private fun trackDownloadStatus(context: Context, downloadId: Long, videoName: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            var downloading = true
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            while (downloading) {
                val query = DownloadManager.Query().setFilterById(downloadId)
                val cursor: Cursor = downloadManager.query(query)

                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                        val uriString = cursor.getString(cursor.getColumnIndexOrThrow(
                            DownloadManager.COLUMN_LOCAL_URI))
                        // Call the suspend function on the main thread
                        withContext(Dispatchers.Main) {
                            playVideoOffline(uriString)  // Play video after downloading
                        }
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        downloading = false
                        lifecycleScope.launch(Dispatchers.Main) {
                            Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                cursor.close()
            }
        }
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
                    Log.d(TAG, "Deleted old video: ${file.name}")
                } else {
                    return
                }
            }
        }
    }



    private suspend fun playVideoOffline(videoUri: String) {
        val videoFileUri = Uri.parse(videoUri)

        // Initialize ExoPlayer on the main thread
        withContext(Dispatchers.Main) {
            val exoPlayer = ExoPlayer.Builder(this@MainActivity).build()

            // Set up the media item
            val mediaItem = MediaItem.fromUri(videoFileUri)
            exoPlayer.setMediaItem(mediaItem)

            // Prepare the player
            exoPlayer.prepare()

            // Play the video
            exoPlayer.playWhenReady = true

            // Bind the player to the view (assuming you have PlayerView in your layout)
            binding.playerView.player = exoPlayer
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        // Clear player resources when the activity is destroyed
        (binding.rvPost.adapter as? HomePostVideoAdapter)?.releasePlayers()
    }

    override fun onStop() {
        super.onStop()
        videoAdapter.releasePlayers() // Release all player instances in the adapter
    }


}