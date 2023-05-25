package com.avicodes.powerconstruction.presentation.di

import com.avicodes.powerconstruct.data.repository.DrawingRepositoryImpl
import com.avicodes.powerconstruct.data.repository.MarkerRepositoryImpl
import com.avicodes.powerconstruct.domain.repository.DrawingRepository
import com.avicodes.powerconstruct.domain.repository.MarkerRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideDrawingRepository(firestore: FirebaseFirestore, storage: FirebaseStorage): DrawingRepository {
        return DrawingRepositoryImpl(
            firestore = firestore,
            storage = storage
        )
    }


    @Provides
    @Singleton
    fun provideMarkerRepository(firestore: FirebaseFirestore): MarkerRepository {
        return MarkerRepositoryImpl(
            firestore = firestore,
        )
    }

}