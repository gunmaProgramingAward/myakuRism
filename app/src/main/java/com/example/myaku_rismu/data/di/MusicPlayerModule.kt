//package com.example.myaku_rismu.data.di
//
//import android.content.ComponentName
//import android.content.Context
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.session.MediaController
//import androidx.media3.session.SessionToken
//import com.example.myaku_rismu.data.repository.MusicPlayerRepositoryImpl
//import com.example.myaku_rismu.data.service.MusicPlaybackService
//import com.example.myaku_rismu.data.useCase.MusicPlayerUseCaseImpl
//import com.example.myaku_rismu.domain.repository.MusicPlayerRepository
//import com.example.myaku_rismu.domain.useCase.MusicPlayerUseCase
//import com.google.common.util.concurrent.ListenableFuture
//import dagger.Binds
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//abstract class MusicPlayerModule {
//
//    @Binds
//    @Singleton
//    abstract fun bindMusicPlayerRepository(
//        repositoryImpl: MusicPlayerRepositoryImpl
//    ): MusicPlayerRepository
//
//    @Binds
//    @Singleton
//    abstract fun bindMusicPlayerUseCase(
//        useCaseImpl: MusicPlayerUseCaseImpl
//    ): MusicPlayerUseCase
//
//    companion object {
//        @Provides
//        @Singleton
//        fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
//            return ExoPlayer.Builder(context).build()
//        }
//
//        @Provides
//        @Singleton
//        fun provideMediaControllerFuture(@ApplicationContext context: Context): ListenableFuture<MediaController> {
//            val sessionToken = SessionToken(
//                context,
//                ComponentName(context, MusicPlaybackService::class.java)
//            )
//            return MediaController.Builder(context, sessionToken).buildAsync()
//        }
//    }
//}