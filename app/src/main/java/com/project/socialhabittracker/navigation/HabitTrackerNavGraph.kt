package com.project.socialhabittracker.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.project.socialhabittracker.data.remote.auth.AuthViewModel
import com.project.socialhabittracker.AppViewModelProvider
import com.project.socialhabittracker.MainViewModel
import com.project.socialhabittracker.ui.add_habit.AddHabit
import com.project.socialhabittracker.ui.add_habit.AddHabitDestination
import com.project.socialhabittracker.ui.auth.LoginDestination
import com.project.socialhabittracker.ui.auth.LoginPage
import com.project.socialhabittracker.ui.auth.SignupDestination
import com.project.socialhabittracker.ui.auth.SignupPage
import com.project.socialhabittracker.ui.edit_habit.EditHabit
import com.project.socialhabittracker.ui.edit_habit.EditHabitDestination
import com.project.socialhabittracker.ui.habit_report.HabitReport
import com.project.socialhabittracker.ui.habit_report.HabitReportDestination
import com.project.socialhabittracker.ui.home.HomeDestination
import com.project.socialhabittracker.ui.home.HomeScreen
import com.project.socialhabittracker.ui.home.HomeViewModel
import com.project.socialhabittracker.ui.settings.Settings
import com.project.socialhabittracker.ui.settings.SettingsDestination
import com.project.socialhabittracker.ui.settings.SettingsViewModel
import com.project.socialhabittracker.ui.theme.AppTheme
import com.project.socialhabittracker.utils.Extensions
import kotlinx.coroutines.launch

@Composable
fun HabitTrackerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val isLoggedIn = Firebase.auth.currentUser != null

    NavHost(
        navController = navController,
        startDestination = if(isLoggedIn) HomeDestination.route else LoginDestination.route,
        modifier = modifier
    ) {
        composable(
            route = HomeDestination.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }
        ) {
            val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val homeUiState by homeViewModel.homeUiState.collectAsState()

            HomeScreen(
                navigateToAddHabit = { navController.navigate(AddHabitDestination.route) },
                navigateToHabitReport = { navController.navigate("${HabitReportDestination.route}/${it}") },
                navigateToEditHabit = { navController.navigate("${EditHabitDestination.route}/${it}") },
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateToSettings = { navController.navigate(SettingsDestination.route) },
                homeUiState = homeUiState,
                deleteHaibt = { homeViewModel.deleteHabit(it) },
                upsertCompletion = { homeViewModel.upsert(it) },
            )
        }

        composable(
            route = AddHabitDestination.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                )
            }
        ) {
            AddHabit(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = HabitReportDestination.routeWithArgs,
            arguments = listOf(navArgument(HabitReportDestination.habitIdArg) {
                type = NavType.IntType
            }),
            enterTransition = {
                scaleIn(
                    animationSpec = tween(300),
                    initialScale = 0.9f
                ) + fadeIn(tween(300))
            }
        ) {
            HabitReport(
                onNavigateUp = { navController.navigateUp() },
                navigateToEditHabit = { navController.navigate("${EditHabitDestination.route}/${it}") }
            )
        }

        composable(
            route = EditHabitDestination.routeWithArgs,
            arguments = listOf(navArgument(EditHabitDestination.habitIdArg) {
                type = NavType.IntType
            }),
            enterTransition = {
                fadeIn( animationSpec = tween(300)) +
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            EditHabit(
                onNavigateUp = { navController.navigateUp() },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = SettingsDestination.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val loadingState by settingsViewModel.loadingState.collectAsStateWithLifecycle()

            val mainViewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)

            val coroutineScope = rememberCoroutineScope()

            Settings(
                onEditProfileClick = {},
                onLogoutClick = {
                    coroutineScope.launch {
                        settingsViewModel.onLogoutClick()
                        navController.navigate(LoginDestination.route) {
                            popUpTo(HomeDestination.route) {
                                inclusive = true
                            }
                        }
                    }
                },
                onThemeChanged = {
                    val appTheme = when(it) {
                        0 -> AppTheme.Blue
                        1 -> AppTheme.Green
                        2 -> AppTheme.Red
                        else -> AppTheme.Blue
                    }
                    mainViewModel.saveTheme(appTheme)
                },
                onRateClick = {},
                onSyncToCloud = {
                    coroutineScope.launch {
                        settingsViewModel.onSyncToCloud()
                    }
                },
                onSyncFromCloud = {
                    coroutineScope.launch {
                        settingsViewModel.onSyncFromCloud()
                    }
                },
                isLoading = loadingState,
                navigateToHome = { navController.navigate(HomeDestination.route) },
                navigateToSettings = { navController.navigate(SettingsDestination.route) },
            )
        }

        composable(
            route = LoginDestination.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }
        ) {
            val authViewModel = AuthViewModel()
            val settingsViewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)

            val context = LocalContext.current

            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            var isLoading by remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()

            LoginPage(
                onLoginClick = {
                    isLoading = true

                    authViewModel.login(
                        email = email,
                        password = password
                    ) { success, errorMessage ->
                        if(success) {
                            coroutineScope.launch {
                                settingsViewModel.onSyncFromCloud()

                                isLoading = false
                                navController.navigate(HomeDestination.route) {
                                    popUpTo(LoginDestination.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        } else {
                            isLoading = false
                            Extensions.showToast(context, errorMessage)
                        }
                    }
                },
                onSignUpClick = { navController.navigate(SignupDestination.route) },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                isLoading = isLoading,
            )
        }

        composable(
            route = SignupDestination.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            val authViewModel = AuthViewModel()

            val context = LocalContext.current

            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var name by remember { mutableStateOf("") }

            var isLoading by remember { mutableStateOf(false) }

            SignupPage(
                onLoginClick = { navController.navigate(LoginDestination.route) },
                onSignUpClick = {
                    isLoading = true

                    authViewModel.signup(
                        email = email,
                        password = password,
                        name = name
                    ) { success, errorMessage ->
                        if(success) {
                            isLoading = false
                            navController.navigate(HomeDestination.route) {
                                popUpTo(LoginDestination.route) {
                                    inclusive = true
                                }
                            }
                        } else {
                            isLoading = false
                            Extensions.showToast(context, errorMessage)
                        }
                    }
                },
                email = email,
                onEmailChange = { email = it },
                name = name,
                onNameChange = { name = it },
                password = password,
                onPasswordChange = { password = it },
                isLoading = isLoading,
            )
        }
    }
}