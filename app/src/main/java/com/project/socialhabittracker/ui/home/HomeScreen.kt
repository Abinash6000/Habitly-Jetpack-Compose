package com.project.socialhabittracker.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.project.socialhabittracker.R
import com.project.socialhabittracker.data.local.habit_completion_db.HabitCompletion
import com.project.socialhabittracker.navigation.NavigationDestination
import com.project.socialhabittracker.ui.theme.spacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToHabitReport: (Int) -> Unit,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    homeUiState: HomeUiState,
    upsertCompletion: (HabitCompletion) -> Unit,

    ) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var showProgressDialog by remember { mutableStateOf(false) }
    var completionForDate by remember {
        mutableStateOf(
            HabitCompletion(
                0,
                convertToMillis(convertToDateMonthYear(Calendar.getInstance().timeInMillis))
            )
        )
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HabitTrackerTopAppBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomBar(
                tabNum = 0,
                bottomBarItems = getTabBarItems(),
                navigateToHome = navigateToHome,
                navigateToSettings = navigateToSettings
            )
        }
    ) { innerPadding ->

        val coroutineScope = rememberCoroutineScope()

        AnimatedVisibility(
            visible = showProgressDialog
        ) {
            AlertDialog(
                onDismissRequest = { showProgressDialog = false },
            ) {
                var valueChanged by remember { mutableStateOf(false) }
                ProgressDialog(
                    progressValue = if (valueChanged) completionForDate.progressValue else "",
                    onValueChange = {
                        completionForDate = completionForDate.copy(progressValue = it)
                        valueChanged = true
                    },
                    onConfirm = {
                        showProgressDialog = false
                        upsertCompletion(completionForDate)
                    },
                    onCancel = { showProgressDialog = false }
                )
            }
        }
        HomeBody(
            habitInfo = homeUiState.habitsList,
            contentPadding = innerPadding,
            upsurt = { upsertCompletion(it) },
            showProgressDialog = { showProgressDialog = it },
            dataToUpsertForConfirmClick = { completionForDate = it },
            navigateToHabitReport = { navigateToHabitReport(it) },
        )
    }
}

@Composable
fun HomeBody(
    habitInfo: List<HabitInfo>,
    contentPadding: PaddingValues = PaddingValues(MaterialTheme.spacing.default),
    upsurt: (HabitCompletion) -> Unit,
    showProgressDialog: (Boolean) -> Unit,
    dataToUpsertForConfirmClick: (HabitCompletion) -> Unit,
    navigateToHabitReport: (Int) -> Unit,
) {
    AnimatedContent(
        targetState = habitInfo.isEmpty()
    ) { state ->
        when (state) {
            false -> {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    DatesCard(title = "Habits")
                    HabitList(
                        habitInfo = habitInfo,
                        upsurt = upsurt,
                        showProgressDialog = showProgressDialog,
                        dataToUpsertForConfirmClick = dataToUpsertForConfirmClick,
                        navigateToHabitReport = { navigateToHabitReport(it) },
                    )
                }
            }

            true -> {

                var show by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(250L)
                    show = true
                }

                if(show) {
                    val composition by rememberLottieComposition(

                        LottieCompositionSpec
                            .RawRes(R.raw.empty_animation)
                    )

                    // to control the animation
                    val progress by animateLottieCompositionAsState(
                        composition,
                        iterations = LottieConstants.IterateForever,
                        isPlaying = true,
                        speed = 0.5f,
                        restartOnPlay = false
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LottieAnimation(
                            composition,
                            progress,
                            modifier = Modifier
                                .size(400.dp)
                                .offset(y = (-44).dp)
                        )
                    }
                }
            }
        }
    }
}
