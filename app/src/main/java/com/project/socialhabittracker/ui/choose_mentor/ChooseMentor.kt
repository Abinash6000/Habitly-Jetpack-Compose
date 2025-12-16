package com.project.socialhabittracker.ui.choose_mentor

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object ChooseMentorDestination {
    const val route = "choose_mentor"
}

@Composable
fun ChooseMentor(
    onMentorChosen: () -> Unit,
    viewModel: ChooseMentorViewModel = viewModel()
) {
    val mentors by viewModel.mentors.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMentors()
    }

    Column {
        Text("Choose your mentor")

        mentors.forEach { mentor ->
            Button(
                onClick = {
                    val uid = Firebase.auth.currentUser?.uid ?: return@Button
                    val name = Firebase.auth.currentUser?.displayName ?: "Disciple"

                    viewModel.chooseMentor(
                        userUid = uid,
                        userName = name,
                        mentorUid = mentor.uid,
                        onDone = onMentorChosen
                    )
                }
            ) {
                Text(mentor.name)
            }
        }
    }
}
