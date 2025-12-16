package com.project.socialhabittracker.ui.mentor.mentor_home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object MentorHomeDestination {
    const val route = "mentor_home"
}

@Composable
fun MentorHome(
    viewModel: MentorHomeViewModel = viewModel()
) {
    val disciples by viewModel.disciples.collectAsState()

    LaunchedEffect(Unit) {
        val uid = Firebase.auth.currentUser?.uid ?: return@LaunchedEffect
        viewModel.loadDisciples(uid)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "My Disciples",
            style = MaterialTheme.typography.headlineSmall
        )

        if (disciples.isEmpty()) {
            Text("No disciples yet")
        } else {
            disciples.forEach {
                Text(text = it.name)
            }
        }
    }
}
