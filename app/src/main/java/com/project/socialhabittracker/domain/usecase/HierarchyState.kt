package com.project.socialhabittracker.domain.usecase

sealed class HierarchyState {
    object Mentor : HierarchyState()
    object Disciple : HierarchyState()
    object Unassigned : HierarchyState()
}
