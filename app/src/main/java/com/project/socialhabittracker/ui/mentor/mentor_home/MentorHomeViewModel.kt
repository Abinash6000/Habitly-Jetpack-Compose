package com.project.socialhabittracker.ui.mentor.mentor_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.socialhabittracker.domain.model.Disciple
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.jvm.java

class MentorHomeViewModel : ViewModel() {

    private val firestore = Firebase.firestore

    private val _disciples = MutableStateFlow<List<Disciple>>(emptyList())
    val disciples: StateFlow<List<Disciple>> = _disciples

    fun loadDisciples(mentorUid: String) {
        viewModelScope.launch {
            val snapshot = firestore.collection("disciples")
                .whereEqualTo("mentorId", mentorUid)
                .get()
                .await()

            _disciples.value = snapshot.toObjects(Disciple::class.java)
        }
    }
}
