package com.example.photochallenge.navigation

import androidx.camera.view.LifecycleCameraController
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navDeepLink
import com.example.photochallenge.activity.MainActivity
import com.example.photochallenge.feature.authentification.presentation.login.PhotoChallengeAuthScreen
import com.example.photochallenge.feature.premiumfeatures.presentation.PremiumScreen
import com.example.photochallenge.feature.standing.presentation.PhotoChallengeStandingScreen
import com.example.photochallenge.feature.takepicture.presentation.CameraPreviewWithPicture
import com.example.photochallenge.feature.voting.presenter.PhotoChallengeVotingScreen


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
    onAuthSuccess: () -> Unit,
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

    // Avoid bottom bar on auth screen
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

                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
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
            startDestination = "auth",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                ) + fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                ) + fadeOut(animationSpec = tween(500))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                ) + fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                ) + fadeOut(animationSpec = tween(500))
            }
        ) {
            composable("auth") {
                PhotoChallengeAuthScreen(onAuthSuccess = {
                    onAuthSuccess.invoke()
                    navController.navigate("standing") {
                        popUpTo("auth") { inclusive = true }
                    }
                })
            }
            composable("standing", deepLinks = listOf(
                navDeepLink {
                    uriPattern = "android-app://androidx.navigation/standing"
                    action = MainActivity.NOTIFICATION_DAILY_STANDING
                }
            )) {
                PhotoChallengeStandingScreen()
            }
            composable("takePicture", deepLinks = listOf(
                navDeepLink {
                    uriPattern = "android-app://androidx.navigation/takePicture"
                    action = MainActivity.NOTIFICATION_DAILY_CHALLENGE
                }
            )) {
                CameraPreviewWithPicture(
                    controller = controller,
                    onVideoCallClick = { onPremiumFeatureClick.invoke("video") },
                    onClickToTakePhoto = { onClickToTakePhoto.invoke() }
                )
            }
            composable("voting") {
                PhotoChallengeVotingScreen(onFinishVoting = {
                    navController.navigate("standing") {
                        popUpTo("voting") { inclusive = true }
                    }
                })
            }
            composable("premium") {
                PremiumScreen { onPremiumFeatureClick.invoke(it) }
            }
        }
    }
}