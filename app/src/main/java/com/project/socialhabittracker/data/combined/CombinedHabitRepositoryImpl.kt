package com.project.socialhabittracker.data.combined

import android.util.Log
import com.project.socialhabittracker.data.local.habit_completion_db.HabitCompletionDao
import com.project.socialhabittracker.data.local.habit_db.HabitDao
import com.project.socialhabittracker.data.remote.firestore.FirestoreService
import kotlinx.coroutines.flow.firstOrNull

class CombinedHabitRepositoryImpl(
    private val habitDao: HabitDao,
    private val habitCompletionDao: HabitCompletionDao,
    private val firestoreService: FirestoreService
) : CombinedHabitRepository {

    // On login and when manually clicks in settings
    override suspend fun syncFromCloud() {
        val remoteHabits = firestoreService.fetchHabits()

        habitDao.insertHabits(remoteHabits)

        remoteHabits.forEach { habit ->
            val completions = firestoreService.fetchCompletions(habit.id.toString())
            habitCompletionDao.insertCompletions(completions)
        }
    }

    // On logout and when manually clicks in settings
    override suspend fun syncToCloud() {
        habitDao.getAllHabits()
            .firstOrNull() // Collects the first emission (the latest snapshot)
            ?.let { localHabits ->
                Log.d("temporaryCompletion", "habits we're uploading is $localHabits")
                firestoreService.uploadHabits(localHabits)
            }

        habitCompletionDao.getAllCompletionDetails()
            .firstOrNull()
            ?.let { localCompletions ->
                Log.d("temporaryCompletion", "Completion we're uploading is $localCompletions")
                firestoreService.uploadCompletions(localCompletions)
            }
    }

    override suspend fun deleteHabitWithCompletions(habitId: Int) {
        firestoreService.deleteHabitWithCompletion(habitId)
        habitDao.delete(habitId)
    }

    // on logout
    override suspend fun clearRoomData() {
        habitDao.clearAllHabits()
        habitCompletionDao.clearAllCompletions()
    }
}
