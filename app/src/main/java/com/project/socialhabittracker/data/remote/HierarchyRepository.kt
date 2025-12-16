package com.project.socialhabittracker.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.project.socialhabittracker.domain.usecase.HierarchyState
import kotlinx.coroutines.tasks.await

class HierarchyRepository(
    private val firestore: FirebaseFirestore
) {
    suspend fun resolve(uid: String): HierarchyState {
        val mentorDoc = firestore.collection("mentors").document(uid).get().await()
        if (mentorDoc.exists()) return HierarchyState.Mentor

        val discipleDoc = firestore.collection("disciples").document(uid).get().await()
        if (discipleDoc.exists()) return HierarchyState.Disciple

        return HierarchyState.Unassigned
    }
}
