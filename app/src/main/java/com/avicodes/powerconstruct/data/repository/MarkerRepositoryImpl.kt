package com.avicodes.powerconstruct.data.repository

import com.avicodes.powerconstruct.data.models.Marker
import com.avicodes.powerconstruct.data.utils.Result
import com.avicodes.powerconstruct.domain.repository.MarkerRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class MarkerRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : MarkerRepository {

    override fun getAllMarkers(drawingId: String) = flow<Result<List<Marker>>> {
        emit(Result.Loading("Updating Drawing"))

        val snapshot = firestore
            .collection("Marker")
            .orderBy("timeCreated", Query.Direction.DESCENDING)
            .whereEqualTo(drawingId, drawingId)
            .get()
            .await()

        val marker = snapshot.toObjects(Marker::class.java)
        emit(Result.Success(marker))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)


    override fun postMarker(
        marker: Marker
    ) = flow<Result<String>> {
        emit(Result.Loading("Loading"))

        firestore.collection("Marker").document(marker.id)
            .set(marker).await()

        emit(Result.Success("Uploaded"))

    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)



}