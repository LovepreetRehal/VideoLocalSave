package com.example.videostoreapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videostoreapplication.databinding.RowCasinoBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
class HomePostVideoAdapter(
    private val videoList: MutableList<String>,
    private val onVideoClick: (String) -> Unit,
    private val recyclerView: RecyclerView // Add RecyclerView reference
) : RecyclerView.Adapter<HomePostVideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(private val binding: RowCasinoBinding) : RecyclerView.ViewHolder(binding.root) {
        private var exoPlayer: ExoPlayer? = null

        fun bind(videoUri: String) {
            // Initialize ExoPlayer
            exoPlayer = ExoPlayer.Builder(binding.root.context).build()

            // Set up the media item
            val mediaItem = MediaItem.fromUri(videoUri)
            exoPlayer?.setMediaItem(mediaItem)

            // Bind the player to the view
            binding.playerView.player = exoPlayer

            // Prepare the player
            exoPlayer?.prepare()
            // Play the video when the item is clicked
            binding.root.setOnClickListener {
                onVideoClick(videoUri)
                playVideo()
            }
        }

        fun playVideo() {
            exoPlayer?.playWhenReady = true
        }

        fun pauseVideo() {
            exoPlayer?.playWhenReady = false
        }

        fun releasePlayer() {
            exoPlayer?.release()
            exoPlayer = null
        }
    }
    fun addVideos(newVideos: List<String>) {
        val startPosition = videoList.size // Get the current size
        videoList.addAll(newVideos) // Add new videos to the list
        notifyItemRangeInserted(startPosition, newVideos.size) // Notify adapter about the new items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = RowCasinoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoList[position])
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holder.releasePlayer() // Release the player when the ViewHolder is recycled
    }

    override fun getItemCount(): Int = videoList.size

    fun pauseAllVideos() {
        for (i in 0 until itemCount) {
            val holder = recyclerView.layoutManager?.findViewByPosition(i) as? VideoViewHolder
            holder?.pauseVideo()
        }
    }

    fun playVideoAt(position: Int) {
        val holder = recyclerView.layoutManager?.findViewByPosition(position) as? VideoViewHolder
        holder?.playVideo()
    }

    fun releasePlayers() {
        for (i in 0 until itemCount) {
            notifyItemChanged(i)
        }
    }
}
