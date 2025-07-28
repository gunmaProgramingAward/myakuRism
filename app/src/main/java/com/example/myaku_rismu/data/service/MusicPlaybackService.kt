package com.example.myaku_rismu.data.service

import android.app.PendingIntent
import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.myaku_rismu.core.base.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlaybackService : MediaSessionService() {
    
    @Inject
    lateinit var player: ExoPlayer
    
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        setupPlayer()
        createMediaSession()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        releaseResources()
        super.onDestroy()
    }
    
    private fun setupPlayer() {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()
        player.setAudioAttributes(audioAttributes, true)
    }
    
    private fun createMediaSession() {
        val sessionActivityPendingIntent = createSessionActivityPendingIntent()
        
        mediaSession = MediaSession.Builder(this, player)
            .setSessionActivity(sessionActivityPendingIntent)
            .build()
    }
    
    private fun createSessionActivityPendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    private fun releaseResources() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
    }
}