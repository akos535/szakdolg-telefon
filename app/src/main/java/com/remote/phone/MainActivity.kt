package com.remote.phone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remote.phone.ui.theme.PhoneClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhoneClientTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    ControlApp(innerPadding)
                }
            }
        }
    }
}

@Composable
fun ControlApp(
	padding: PaddingValues,
	viewModel: ControlPanelViewModel = viewModel()
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME)}

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
					icon = {
						Icon(
							imageVector = it.icon,
							contentDescription = it.label
						)
					},
					label = { Text(it.label) },
					selected = it == currentDestination,
                    onClick = {currentDestination = it}
				)
            }
        }
    ) {
		when(currentDestination) {
			AppDestinations.HOME -> MainPage(
				selectedOption = viewModel.selectedMenuOption,
				onMenuOptionSelected = viewModel::selectMenuOption
			)
			AppDestinations.CONNECT -> ConnectPage()
		}
	}
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector
) {
    HOME("Placeholder", Icons.Outlined.Home),
    CONNECT("Csatlakozás", Icons.Outlined.Settings)
}