package edu.farmingdale.pizzapartybottomnavbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.farmingdale.pizzapartybottomnavbar.ui.theme.PizzaPartyBottomNavBarTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzaPartyBottomNavBarTheme {
                val navController: NavHostController = rememberNavController()
                var buttonsVisible by remember { mutableStateOf(true) }

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            NavigationDrawerItem(
                                label = { Text("Pizza Order") },
                                selected = false,
                                onClick = {
                                    // Handle navigation for Pizza Order
                                    navController.navigate("PizzaScreen")
                                    coroutineScope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("GPA") },
                                selected = false,
                                onClick = {
                                    // Handle navigation for Pizza Order
                                    navController.navigate("GpaAppScreen")
                                    coroutineScope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("SCREEN 3") },
                                selected = false,
                                onClick = {
                                    // Handle navigation for GPA App
                                    navController.navigate("Screen3")
                                    coroutineScope.launch { drawerState.close() }
                                }
                            )
                            // Add more NavigationDrawerItem as needed
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Pizza Party App") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        coroutineScope.launch {
                                            if (drawerState.isClosed) drawerState.open()
                                            else drawerState.close()
                                        }
                                    }) {
                                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            if (buttonsVisible) {
                                BottomBar(
                                    navController = navController,
                                    state = buttonsVisible,
                                    modifier = Modifier
                                )
                            }
                        }
                    ) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            NavigationGraph(navController = navController) { isVisible ->
                                buttonsVisible = isVisible
                            }
                        }
                    }
                }
            }
        }
    }
}
