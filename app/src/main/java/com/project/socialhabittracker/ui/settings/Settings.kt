package com.project.socialhabittracker.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.socialhabittracker.R
import com.project.socialhabittracker.navigation.NavigationDestination
import com.project.socialhabittracker.ui.home.BottomBar
import com.project.socialhabittracker.ui.home.getTabBarItems
import com.project.socialhabittracker.ui.theme.spacing

object SettingsDestination : NavigationDestination {
    override val route: String = "settings"
    override val titleRes: Int = R.string.settings
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onThemeChanged: (Int) -> Unit,
    onRateClick: () -> Unit,
    onSyncToCloud: () -> Unit,
    onSyncFromCloud: () -> Unit,
    isLoading: Boolean,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            )
        },
        bottomBar = {
            BottomBar(
                tabNum = 1,
                bottomBarItems = getTabBarItems(),
                navigateToHome = navigateToHome,
                navigateToSettings = navigateToSettings,
            )
        }
    ) { padding ->

        val context = LocalContext.current
        var showAboutDialog by remember { mutableStateOf(false) }
        var showThemeDialog by remember { mutableStateOf(false) }
        val scrollState = rememberScrollState()

        AnimatedContent(isLoading) { isLoading ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(padding)
                        .padding(MaterialTheme.spacing.medium)
                ) {
                    // preferences section
                    Text("Preferences", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                    OutlinedButton(
                        onClick = {
                            showThemeDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.paint),
                            contentDescription = "Change Theme",
                            modifier = Modifier.size(MaterialTheme.spacing.large)
                        )
                        Spacer(Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Change Theme",
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    // backup section
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    Text("Backup", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                    OutlinedButton(
                        onClick = onSyncToCloud,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.sync),
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.spacing.large)
                        )
                        Spacer(Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Sync To Cloud",
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    OutlinedButton(
                        onClick = onSyncFromCloud,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.sync),
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.spacing.large)
                        )
                        Spacer(Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Sync From Cloud",
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    // support section
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    Text("Support", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                    OutlinedButton(
                        onClick = {
                            sendFeedback(context)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Email, contentDescription = null)
                        Spacer(Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Send Feedback",
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    OutlinedButton(
                        onClick = onRateClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null)
                        Spacer(Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Rate Us",
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    OutlinedButton(
                        onClick = { showAboutDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null)
                        Spacer(Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "About",
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                    // Account Section
                    Text("Account", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

                    OutlinedButton(
                        onClick = onEditProfileClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null)
                        Spacer(Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Edit Profile",
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    OutlinedButton(
                        onClick = onLogoutClick,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                        Spacer(Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = "Logout",
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.smallMedium))

                    Text(
                        text = "App version: 1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                AnimatedVisibility(showAboutDialog) {
                    AboutDialog(
                        onDismiss = { showAboutDialog = false }
                    )
                }

                AnimatedVisibility(showThemeDialog) {
                    ChangeThemeDialog(
                        onDismiss = { showThemeDialog = false },
                        onThemeSelected = onThemeChanged
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeThemeDialog(
    onDismiss: () -> Unit,
    onThemeSelected: (themeId: Int) -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(MaterialTheme.spacing.medium),
            tonalElevation = MaterialTheme.spacing.small,
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Change Theme",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.mediumLarge),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val themes = listOf(
                        R.drawable.theme_blue,
                        R.drawable.theme_green,
                        R.drawable.theme_red,
                    )
                    themes.forEachIndexed { index, imageRes ->
                        Box(
                            modifier = Modifier
                                .clickable {
                                    onThemeSelected(index)
                                    onDismiss()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = "Theme $index",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = { Text("About This App") },
        text = {
            Text("This app is built with Jetpack Compose and love.\nVersion 1.0.0\n© 2025 Abinash Inc.")
        }
    )
}

fun sendFeedback(context: Context) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Only email apps handle this.
        putExtra(Intent.EXTRA_EMAIL, arrayOf("shyamadev7000@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, "App Feedback")
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
    }
}