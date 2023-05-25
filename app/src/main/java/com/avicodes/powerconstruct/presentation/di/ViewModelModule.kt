package com.avicodes.powerconstruction.presentation.di

import com.avicodes.powerconstruct.domain.repository.DrawingRepository
import com.avicodes.powerconstruct.domain.repository.MarkerRepository
import com.avicodes.powerconstruct.presentation.ui.MainActivityViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ViewModelModule {
    @Singleton
    @Provides
    fun provideMainActivityViewModelFactory(
        drawingRepository: DrawingRepository,
        markerRepository: MarkerRepository
    ) : MainActivityViewModelFactory {
        return MainActivityViewModelFactory(
            drawingRepository = drawingRepository,
            markerRepository = markerRepository
        )
    }
}