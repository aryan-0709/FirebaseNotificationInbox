package com.example.firebasenotificationinbox.ui.inbox

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firebasenotificationinbox.ui.details.DetailsScreen

@Composable
fun NotificationNavGraph(
    viewModel: NotificationViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inbox"
    ) {
        composable("inbox") {
            InboxScreen(
                viewModel = viewModel,
                onNotificationClick = { notificationId ->
                    navController.navigate("details/$notificationId")
                }
            )
        }

        composable(
            route = "details/{notificationId}",
            arguments = listOf(
                navArgument("notificationId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val notificationId =
                backStackEntry.arguments?.getString("notificationId")

            if (notificationId != null) {
                DetailsScreen(
                    notificationId = notificationId,
                    viewModel = viewModel
                )
            }
        }
    }
}