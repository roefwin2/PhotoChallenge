package com.example.photochallenge.navigation

import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.photochallenge.authentification.presentation.login.PhotoChallengeAuthScreen
import com.example.photochallenge.premiumfeatures.presentation.PremiumScreen
import com.example.photochallenge.standing.presentation.PhotoChallengeStandingScreen
import com.example.photochallenge.takepicture.presentation.CameraPreviewWithPicture
import com.example.photochallenge.voting.presenter.PhotoChallengeVotingScreen


data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun PhotoChallengeNavigation(
    navController: NavHostController,
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    onClickToTakePhoto: (() -> Unit),
    onPremiumFeatureClick: ((String) -> Unit)
) {
    val bottomNavItems = listOf(
        BottomNavItem("standing", Icons.Default.Home, "Standing"),
        BottomNavItem("takePicture", Icons.Default.PhotoCamera, "Photo"),
        BottomNavItem("voting", Icons.Default.HowToVote, "Vote"),
        BottomNavItem("premium", Icons.Default.Star, "Premium")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // N'afficher la BottomBar que sur certains écrans
    val shouldShowBottomBar = currentRoute != "auth"

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Pop jusqu'au départ pour éviter d'empiler les écrans
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Évite les copies multiples de la même destination
                                    launchSingleTop = true
                                    // Restaure l'état lors de la reselection
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController,
            startDestination = "auth"
        ) {
            composable("auth") {
                PhotoChallengeAuthScreen(onAuthSuccess = {
                    navController.navigate("standing") {
                        popUpTo("auth") { inclusive = true }
                    }
                })
            }
            composable("standing") {
                PhotoChallengeStandingScreen()
            }
            composable("takePicture") {
                CameraPreviewWithPicture(
                    controller = controller,
                    onClickToTakePhoto = { onClickToTakePhoto.invoke() }
                )
            }
            composable("voting") {
                PhotoChallengeVotingScreen()
            }
            composable("premium") {
                PremiumScreen { onPremiumFeatureClick.invoke(it) }
            }
        }
    }
}