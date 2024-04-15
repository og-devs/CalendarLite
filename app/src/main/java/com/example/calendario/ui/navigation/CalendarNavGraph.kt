package com.example.calendario.ui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.calendario.ui.screen.home.HomeDestination
import com.example.calendario.ui.screen.home.HomeScreen
import com.example.calendario.ui.screen.item.details.ItemDetailsDestination
import com.example.calendario.ui.screen.item.details.ItemDetailsScreen
import com.example.calendario.ui.screen.item.edit.ItemEditDestination
import com.example.calendario.ui.screen.item.edit.ItemEditScreen
import com.example.calendario.ui.screen.item.entry.ItemEntryDestination
import com.example.calendario.ui.screen.item.entry.ItemEntryScreen

@Composable
fun CalendarNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                },
                modifier = modifier
            )
        }
        composable(route = ItemEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                modifier = modifier
            )
        }
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.ITEM_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() },
                modifier = modifier
            )
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.ITEM_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
                modifier = modifier
            )
        }
    }
}