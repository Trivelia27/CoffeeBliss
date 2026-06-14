package com.example.coffeebliss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.coffeebliss.ui.CoffeeViewModel
import com.example.coffeebliss.ui.CoffeeViewModelFactory
import com.example.coffeebliss.ui.Screen
import com.example.coffeebliss.ui.screens.*
import com.example.coffeebliss.ui.theme.CoffeeBlissTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CoffeeViewModel by viewModels {
        val app = application as CoffeeApplication
        CoffeeViewModelFactory(app.repository, app.sessionManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoffeeBlissTheme {
                CoffeeApp(viewModel)
            }
        }
    }
}

@Composable
fun CoffeeApp(viewModel: CoffeeViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController, viewModel)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController, viewModel)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController, viewModel)
        }

        composable(
            route     = Screen.MemberDetail.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("memberId") ?: 0
            MemberDetailScreen(navController, viewModel, id)
        }

        composable(
            route     = Screen.MemberCard.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("memberId") ?: 0
            MemberCardScreen(navController, viewModel, id)
        }

        composable(
            route     = Screen.Transaction.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("memberId") ?: 0
            TransactionScreen(navController, viewModel, id)
        }

        composable(
            route     = Screen.Rewards.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("memberId") ?: 0
            RewardScreen(navController, viewModel, id)
        }

        composable(
            route     = Screen.History.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("memberId") ?: 0
            HistoryScreen(navController, viewModel, id)
        }

        composable(
            route     = Screen.Profile.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("memberId") ?: 0
            ProfileScreen(navController, viewModel, id)
        }
    }
}
