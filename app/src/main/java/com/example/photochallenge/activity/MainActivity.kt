@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.photochallenge.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.photochallenge.core.presentation.PaywallOverlay
import com.example.photochallenge.navigation.PhotoChallengeNavigation
import com.example.photochallenge.takepicture.presentation.PhotoBottomSheetContent
import com.example.photochallenge.ui.theme.PhotoChallengeTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermission()) {
            requestPermissions(CAMERAX_PERMISSION, 0)
        }
        enableEdgeToEdge()
        setContent {
            PhotoChallengeTheme {
                val coroutineScope = rememberCoroutineScope()
                val viewModel: MainViewModel = koinViewModel()
                val state = viewModel.state.collectAsState()
                val navController = rememberNavController()
                val scaffoldState = rememberBottomSheetScaffoldState(
                    bottomSheetState = rememberStandardBottomSheetState(skipHiddenState = false)
                )
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(
                            LifecycleCameraController.IMAGE_CAPTURE
                        )
                    }
                }
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetPeekHeight = 0.dp,
                    sheetContent = {
                        PaywallOverlay(
                            visible = state.value.isPaywallVisible,
                            onSubscribeClick = {
                                viewModel.onDeletedPhoto()
                                coroutineScope.launch {
                                    scaffoldState.bottomSheetState.hide()
                                }
                            }
                        ) {
                            PhotoBottomSheetContent(
                                bitmap = state.value.bitmap,
                                modifier = Modifier.fillMaxWidth(),
                                onValidatePhoto = {
                                    viewModel.onSavePhoto()
                                    coroutineScope.launch {
                                        scaffoldState.bottomSheetState.hide()
                                    }
                                    navController.navigate("voting")
                                },
                                retryPhoto = {
                                    viewModel.onPremiumFeature()
                                }
                            )
                        }
                    },
                ) { paddingValues ->
                    PhotoChallengeNavigation(
                        navController = navController,
                        controller = controller,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        onClickToTakePhoto = {
                            takePhoto(controller) {
                                coroutineScope.launch {
                                    viewModel.onTakenPhoto(it)
                                    scaffoldState.bottomSheetState.expand()
                                }
                            }
                        },
                        onPremiumFeatureClick = {
                            coroutineScope.launch {
                                viewModel.onPremiumFeature()
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun takePhoto(controller: LifecycleCameraController, onPhotoTaken: (Bitmap) -> Unit) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                        postScale(-1f, 1f)
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )
                    onPhotoTaken.invoke(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("MainActivity", "Error taking photo", exception)
                }
            })
    }

    private fun hasRequiredPermission(): Boolean {
        return CAMERAX_PERMISSION.all {
            ContextCompat.checkSelfPermission(
                baseContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
        )
    }
}