package com.example.photochallenge.navigation

import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.photochallenge.authentification.presentation.login.PhotoChallengeAuthScreen
import com.example.photochallenge.takepicture.presentation.CameraPreviewWithPicture

@Composable
fun PhotoChallengeNavigation(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    onClickToTakePhoto: (() -> Unit)
) {
    val navController = rememberNavController()
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
    }
}