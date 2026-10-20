package com.example.libraryapp.ui.navigation

import androidx.annotation.StringRes
import com.example.libraryapp.R
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.libraryapp.ui.books.BooksScreen
import com.example.libraryapp.ui.details.BookDetailsScreen
import com.example.libraryapp.ui.favorites.FavoritesScreen
import com.example.libraryapp.ui.profile.ProfileScreen

private data class BottomTab(
    val route: String,
    @StringRes val titleRes: Int,
    val icon: ImageVector
)

private val bottomTabs = listOf(
    BottomTab(route = Screen.Books.route, titleRes = R.string.tab_books, icon = Icons.AutoMirrored.Filled.MenuBook),
    BottomTab(route = Screen.Favorites.route, titleRes = R.string.tab_favorites, icon = Icons.Filled.Favorite),
    BottomTab(route = Screen.Profile.route, titleRes = R.string.tab_profile, icon = Icons.Filled.Person)
)

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomTabs.forEach { tab ->
                    val selected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == tab.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = stringResource(tab.titleRes)
                            )
                        },
                        label = { Text(stringResource(tab.titleRes)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Books.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Books.route) {
                BooksScreen(
                    onBookClick = {
                        bookId ->
                        navController.navigate(Screen.BookDetails.createRoute(bookId))
                    }
                )
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            composable(
                route = Screen.BookDetails.route,
                arguments = listOf(
                    navArgument(Screen.BookDetails.ARG_BOOK_ID) {
                        type = NavType.StringType
                    }
                )
            ) {
                BookDetailsScreen(
                    onBackClick = { navController.popBackStack() }
                    )
            }
        }
    }
}