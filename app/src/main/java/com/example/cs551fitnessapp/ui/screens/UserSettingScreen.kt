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
import com.example.cs551fitnessapp.ui.viewmodels.ThemeViewModel

import com.example.cs551fitnessapp.scheduler.NotificationScheduler


@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    val application = context.applicationContext as Application


    val repository = remember(context) {

        context.provideRepository()

    }

    val scheduler = remember(context) {

        NotificationScheduler(context)

    }

    val sessionDao = remember(context) {
        AppDatabase.getDatabase(context).sessionDao()
    }


    val viewModel: NotificationSettingsViewModel = viewModel(
        factory = NotificationSettingsViewModel.provideFactory(
            application,
            repository,
            scheduler,
            sessionDao
        )
    )


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    SettingsScreenContent(

        state = uiState,

        onWeeklyNotiChange = viewModel::onNotiPeriodicToggled,

        onUpcomingSessionNotiChange = viewModel::onNotiDynamicToggled,

        onBack = onBack

    )
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(

    state: NotificationSettingsUiState,

    onWeeklyNotiChange: (Boolean) -> Unit,

    onUpcomingSessionNotiChange: (Boolean) -> Unit,

    onBack: () -> Unit

) {

    val themeViewModel: ThemeViewModel = viewModel()


    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(

                        "Settings",

                        fontWeight = FontWeight.Bold

                    )

                },

                navigationIcon = {

                    IconButton(onClick = onBack) {

                        Icon(

                            Icons.Default.ArrowBack,

                            contentDescription = "Back"

                        )

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

                .padding(

                    horizontal = 20.dp,

                    vertical = 16.dp

                ),


            verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {



            SettingsToggleRow(

                label = "Weekly Summary Notification",

                subtitle = "Receive weekly workout summary updates",

                checked = state.isPeriodicEnabled,

                onChecked = onWeeklyNotiChange

            )



            SettingsToggleRow(

                label = "Upcoming Client Session Notification",

                subtitle = "Get notified before scheduled sessions",

                checked = state.isDynamicEnabled,

                onChecked = onUpcomingSessionNotiChange

            )



            SettingsToggleRow(

                label = "Dark Theme",

                subtitle = "Switch app appearance between light and dark mode",

                checked = themeViewModel.isDarkTheme.value,

                onChecked = themeViewModel::toggleTheme

            )

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


        Column(

            modifier = Modifier.weight(1f)

        ) {


            Text(

                text = label,

                style = MaterialTheme.typography.bodyLarge,

                fontWeight = FontWeight.Medium

            )


            Text(

                text = subtitle,

                style = MaterialTheme.typography.bodySmall,

                color = MaterialTheme.colorScheme.onSurfaceVariant

            )

        }


        Switch(

            checked = checked,

            onCheckedChange = onChecked

        )

    }

}