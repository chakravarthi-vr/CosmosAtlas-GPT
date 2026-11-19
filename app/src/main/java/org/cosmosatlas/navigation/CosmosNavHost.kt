package org.cosmosatlas.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.cosmosatlas.core.designsystem.theme.NeonCyan
import org.cosmosatlas.core.designsystem.theme.SpaceBlack
import org.cosmosatlas.core.designsystem.theme.SpaceDarkSurface
import org.cosmosatlas.core.designsystem.theme.TextMuted
import org.cosmosatlas.core.designsystem.theme.TextPrimary
import org.cosmosatlas.feature.catalog.ui.CatalogScreen
import org.cosmosatlas.feature.dailycosmos.ui.DailyCosmosScreen
import org.cosmosatlas.feature.orrery.ui.OrreryScreen
import org.cosmosatlas.feature.solar.ui.SolarDashboardScreen

sealed class CosmosDestination(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Orrery : CosmosDestination("orrery", "Orrery", Icons.Default.Public)
    data object Catalog : CosmosDestination("catalog", "Deep Sky", Icons.Default.Explore)
    data object Solar : CosmosDestination("solar", "Sun & Weather", Icons.Default.WbSunny)
    data object DailyCosmos : CosmosDestination("daily_cosmos", "Daily APOD", Icons.Default.AutoAwesome)
}

val navDestinations = listOf(
    CosmosDestination.Orrery,
    CosmosDestination.Catalog,
    CosmosDestination.Solar,
    CosmosDestination.DailyCosmos
)

@Composable
fun CosmosNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = SpaceDarkSurface,
                contentColor = TextPrimary
            ) {
                navDestinations.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                        label = { Text(text = screen.title, style = MaterialTheme.typography.labelSmall) },
                        selected = selected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SpaceBlack,
                            selectedTextColor = NeonCyan,
                            indicatorColor = NeonCyan,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CosmosDestination.Orrery.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(CosmosDestination.Orrery.route) {
                OrreryScreen()
            }
            composable(CosmosDestination.Catalog.route) {
                CatalogScreen()
            }
            composable(CosmosDestination.Solar.route) {
                SolarDashboardScreen()
            }
            composable(CosmosDestination.DailyCosmos.route) {
                DailyCosmosScreen()
            }
        }
    }
}
