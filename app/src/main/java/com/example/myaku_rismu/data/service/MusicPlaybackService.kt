package com.example.myaku_rismu.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.base.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlaybackService : MediaSessionService() {

    @Inject
    lateinit var exoPlayer: ExoPlayer

    private var mediaSession: MediaSession? = null

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "music_playback_channel"
        private const val CHANNEL_NAME = "Music Playback"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicPlaybackService", "=== SERVICE CREATED ===")

        createNotificationChannel()
        mediaSession = MediaSession.Builder(this, exoPlayer).build()
        Log.d("MusicPlaybackService", "MediaSession created")

        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.d("MusicPlaybackService", "onIsPlayingChanged: $isPlaying")
                if (isPlaying) {
                    Log.d("MusicPlaybackService", "Starting foreground with notification")
                    startForeground(NOTIFICATION_ID, createNotification())
                } else {
                    Log.d("MusicPlaybackService", "Stopping foreground")
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                val stateName = when (playbackState) {
                    Player.STATE_IDLE -> "IDLE"
                    Player.STATE_BUFFERING -> "BUFFERING"
                    Player.STATE_READY -> "READY"
                    Player.STATE_ENDED -> "ENDED"
                    else -> "UNKNOWN($playbackState)"
                }
                Log.d("MusicPlaybackService", "onPlaybackStateChanged: $stateName")
                
                when (playbackState) {
                    Player.STATE_READY -> {
                        if (exoPlayer.isPlaying) {
                            Log.d("MusicPlaybackService", "Ready and playing, starting foreground")
                            startForeground(NOTIFICATION_ID, createNotification())
                        }
                    }

                    Player.STATE_ENDED -> {
                        Log.d("MusicPlaybackService", "Playback ended, stopping foreground")
                        stopForeground(STOP_FOREGROUND_REMOVE)
                    }

                    Player.STATE_BUFFERING, Player.STATE_IDLE -> {
                        Log.d("MusicPlaybackService", "Buffering or idle state")
                    }
                }
            }
        })
        Log.d("MusicPlaybackService", "ExoPlayer listener added")
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            release()
            mediaSession = null
        }
        exoPlayer.release()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Music playback controls"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        Log.d("MusicPlaybackService", "=== CREATING NOTIFICATION ===")
        
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val currentTrack = exoPlayer.currentMediaItem
        val trackTitle = currentTrack?.mediaMetadata?.title?.toString() ?: "音楽再生中"
        val trackArtist = currentTrack?.mediaMetadata?.artist?.toString() ?: "MyakuRism"
        
        Log.d("MusicPlaybackService", "Track title: $trackTitle")
        Log.d("MusicPlaybackService", "Track artist: $trackArtist")
        Log.d("MusicPlaybackService", "ExoPlayer isPlaying: ${exoPlayer.isPlaying}")

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(trackTitle)
            .setContentText(trackArtist)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setCategory(NotificationCompat.CATEGORY_TRANSPORT)
            .setOnlyAlertOnce(true)
            .setOngoing(exoPlayer.isPlaying)
            .build()
            
        Log.d("MusicPlaybackService", "Notification created successfully")
        return notification
    }
}