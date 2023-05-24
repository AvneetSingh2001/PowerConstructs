package com.avicodes.powerconstruct.data.repository

import androidx.core.net.toUri
import com.avicodes.powerconstruct.data.models.Drawing
import com.avicodes.powerconstruct.domain.repository.DrawingRepository
import kotlinx.coroutines.flow.flow
import com.avicodes.powerconstruct.data.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID

class DrawingRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : DrawingRepository {

    override fun getAllDrawings() = flow<Result<List<Drawing>>> {
        emit(Result.Loading("Updating Drawing"))

        val snapshot = firestore
            .collection("Drawing")
            .orderBy("timeAdded", Query.Direction.DESCENDING)
            .get()
            .await()

        val article = snapshot.toObjects(Drawing::class.java)
        emit(Result.Success(article))

    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)


    override fun postDrawing(title: String, imgUri: String) = flow<Result<String>> {
        emit(Result.Loading("Loading"))
        val id = UUID.randomUUID().toString()

        val imgUrl = storage
            .getReference("drawing/${id}")
            .putFile(imgUri.toUri())
            .await()
            .storage
            .downloadUrl
            .await()
            .toString()

        val drawing = Drawing(
            id = id,
            timeAdded = System.currentTimeMillis().toString(),
            name = title,
            url = imgUrl,
            markerCount = 0
        )

        firestore.collection("Drawing").document(id)
            .set(drawing).await()

        emit(Result.Success("Uploaded"))

    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)


}