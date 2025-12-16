package com.project.socialhabittracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.socialhabittracker.data.preferences.ThemeRepository
import com.project.socialhabittracker.domain.usecase.HierarchyState
import com.project.socialhabittracker.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel(
    private val themeRepository: ThemeRepository
) : ViewModel(){

    private val firestore = Firebase.firestore

    private val _hierarchyState =
        MutableStateFlow<HierarchyState>(HierarchyState.Unassigned)
    val hierarchyState: StateFlow<HierarchyState> = _hierarchyState

    private val _appTheme = MutableStateFlow("Blue")
    val appTheme: StateFlow<String> = _appTheme

    init {
        getTheme()
    }

    fun saveTheme(theme: AppTheme) {
        viewModelScope.launch {
            themeRepository.saveTheme(theme)
        }
    }

    private fun getTheme() {
        viewModelScope.launch {
            themeRepository.getTheme().collect { theme ->
                _appTheme.value = theme
            }
        }
    }

    fun setHierarchyState(state: HierarchyState) {
        _hierarchyState.value = state
    }

    fun resolveHierarchy(uid: String) {
        viewModelScope.launch {
            // reset state
            _hierarchyState.value = HierarchyState.Unassigned

            // check mentor
            val mentorDoc = firestore
                .collection("mentors")
                .document(uid)
                .get()
                .await()

            if (mentorDoc.exists()) {
                _hierarchyState.value = HierarchyState.Mentor
                return@launch
            }

            // check disciple
            val discipleDoc = firestore
                .collection("disciples")
                .document(uid)
                .get()
                .await()

            if (discipleDoc.exists()) {
                _hierarchyState.value = HierarchyState.Disciple
                return@launch
            }

            // neither
            _hierarchyState.value = HierarchyState.Unassigned
        }
    }

}