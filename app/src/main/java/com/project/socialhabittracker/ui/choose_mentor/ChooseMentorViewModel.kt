package com.project.socialhabittracker.ui.choose_mentor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.socialhabittracker.domain.model.Disciple
import com.project.socialhabittracker.domain.model.Mentor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.jvm.java

class ChooseMentorViewModel : ViewModel() {

    private val firestore = Firebase.firestore

    private val _mentors = MutableStateFlow<List<Mentor>>(emptyList())
    val mentors: StateFlow<List<Mentor>> = _mentors

    fun loadMentors() {
        viewModelScope.launch {
            val snapshot = firestore.collection("mentors").get().await()
            _mentors.value = snapshot.documents.mapNotNull {
                it.toObject(Mentor::class.java)
            }
        }
    }

    fun chooseMentor(
        userUid: String,
        userName: String,
        mentorUid: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            val disciple = Disciple(
                uid = userUid,
                name = userName,
                mentorId = mentorUid,
                joinedAt = System.currentTimeMillis()
            )

            firestore.collection("disciples")
                .document(userUid)
                .set(disciple)
                .await()

            onDone()
        }
    }
}
