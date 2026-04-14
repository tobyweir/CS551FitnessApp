package com.example.cs551fitnessapp.ui.screens

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cs551fitnessapp.database.AppDatabase
import com.example.cs551fitnessapp.database.provideRepository
import com.example.cs551fitnessapp.ui.viewmodels.states.NotificationSettingsUiState
import com.example.cs551fitnessapp.ui.viewmodels.NotificationSettingsViewModel
import com.example.cs551fitnessapp.scheduler.NotificationScheduler


@Composable
fun SettingsScreen(
    modifier: Modifier,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Build the repository once, then hand it to the factory.
    val repository = remember(context) { context.provideRepository() }
    val scheduler = remember(context) { NotificationScheduler(context) }
    val appointmentDao = remember(context) { AppDatabase.getDatabase(context).appointmentDao() }

    val viewModel: NotificationSettingsViewModel = viewModel(
        factory = NotificationSettingsViewModel.provideFactory(
            application,
            repository,
            scheduler,
            appointmentDao
        )
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreenContent(
        state = uiState,
        onWeeklyNotiChange = viewModel::onNotiPeriodicToggled,
        onUpcomingSessionNotiChange = viewModel::onNotiDynamicToggled,
        onBack = onBack,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    state: NotificationSettingsUiState,
    onWeeklyNotiChange: (Boolean) -> Unit,
    onUpcomingSessionNotiChange: (Boolean) -> Unit,
    onBack: () -> Unit,
    modifier : Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Notification Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SettingsToggleRow(
                label = "Weekly Summary Notification",
                subtitle = "Weekly training summary notifications",
                checked = state.isPeriodicEnabled,
                onChecked = onWeeklyNotiChange
            )
            SettingsToggleRow(
                label = "Upcoming Client Session Notification",
                subtitle = "Upcoming client session notifications",
                checked = state.isDynamicEnabled,
                onChecked = onUpcomingSessionNotiChange
            )
//            Button(
//                onClick =  ontest,
//
//                modifier = Modifier.padding(16.dp)) {
//                Text("Test")
//            }
//            SettingsToggleRow(
//                label = "Dark Theme",
//                subtitle = "Upcoming client session notifications",
//                checked = state.isDynamicEnabled,
//                onChecked = onUpcomingSessionNotiChange
//            )
        }
    }
}

@Composable
private fun SettingsToggleRow(
    label: String,
    subtitle: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(
                subtitle, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onChecked)
    }
}
