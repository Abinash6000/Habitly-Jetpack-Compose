package com.project.socialhabittracker.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import com.project.socialhabittracker.R
import com.project.socialhabittracker.data.local.habit_completion_db.HabitCompletion
import com.project.socialhabittracker.ui.settings.SettingsDestination

@Composable
fun HabitList(
    habitInfo: List<HabitInfo>,
    upsurt: (HabitCompletion) -> Unit,
    showProgressDialog: (Boolean) -> Unit,
    dataToUpsertForConfirmClick: (HabitCompletion) -> Unit,
    navigateToHabitReport: (Int) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = habitInfo, key = { it.habit.id }) { habitInfo ->
            HabitCompletionCard(
                habitInfo = habitInfo,
                dropDownItems = listOf(DropDownItem("Edit"), DropDownItem("Delete")),
                upsert = upsurt,
                showProgressDialog = showProgressDialog,
                dataToUpsertForConfirmClick = dataToUpsertForConfirmClick,
                navigateToHabitReport = { navigateToHabitReport(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitTrackerTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

data class TabBarItem(
    val title: Int,
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)

fun getTabBarItems(): List<TabBarItem> {
    val homeTab = TabBarItem(title = R.string.home, route = HomeDestination.route, selectedIcon = R.drawable.filled_home, unselectedIcon = R.drawable.outlined_home)
//    val overallReportTab = TabBarItem(title = R.string.report, route = OverallReport.route, selectedIcon = R.drawable.filled_bar_chart, unselectedIcon = R.drawable.filled_bar_chart)
    val settingsTab = TabBarItem(title = R.string.settings, route = SettingsDestination.route, selectedIcon = R.drawable.settings_filled, unselectedIcon = R.drawable.outlined_settings)

    return listOf(homeTab, /*overallReportTab,*/ settingsTab)
}

@Composable
fun BottomBar(
    tabNum: Int,
    bottomBarItems: List<TabBarItem>,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit
) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(tabNum)
    }
    NavigationBar {
        bottomBarItems.forEachIndexed { index, tabBarItem ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    when(tabBarItem.route) {
                        HomeDestination.route -> navigateToHome()
                        SettingsDestination.route -> navigateToSettings()
                    }
                },
                icon = {
                    TabBarIconView(
                        isSelected = selectedTabIndex == index,
                        selectedIcon = ImageVector.vectorResource(tabBarItem.selectedIcon),
                        unselectedIcon = ImageVector.vectorResource(tabBarItem.unselectedIcon),
                        title = stringResource(id = tabBarItem.title)
                    )
                },
                label = { Text(text = stringResource(tabBarItem.title)) })
        }
    }
}

@Composable
fun TabBarIconView(
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    title: String
) {
    Icon(
        imageVector = if (isSelected) {selectedIcon} else {unselectedIcon},
        contentDescription = title
    )
}