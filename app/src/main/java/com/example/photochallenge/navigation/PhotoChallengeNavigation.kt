package com.example.photochallenge.navigation

import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.photochallenge.authentification.presentation.login.PhotoChallengeAuthScreen
import com.example.photochallenge.takepicture.presentation.CameraPreviewWithPicture
import com.example.photochallenge.voting.presenter.PhotoChallengeVotingScreen

@Composable
fun PhotoChallengeNavigation(
    navController: NavHostController,
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    onClickToTakePhoto: (() -> Unit)
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = "auth"
    ) {
        composable("auth") {
            PhotoChallengeAuthScreen(onAuthSuccess = {
                navController.navigate("takePicture")
            })
        }
        composable("takePicture") {
            CameraPreviewWithPicture(controller = controller, onClickToTakePhoto = {
                onClickToTakePhoto.invoke()
            })
        }
        composable("voting") {
            PhotoChallengeVotingScreen()
        }
    }
}