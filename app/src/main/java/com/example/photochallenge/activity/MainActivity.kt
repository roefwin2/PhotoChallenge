@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.photochallenge.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.photochallenge.authentification.presentation.login.PhotoChallengeAuthScreen
import com.example.photochallenge.takepicture.presentation.CameraPreview
import com.example.photochallenge.takepicture.presentation.PhotoBottomSheetContent
import com.example.photochallenge.ui.theme.PhotoChallengeTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

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
                val scaffoldState = rememberBottomSheetScaffoldState()
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
                            PhotoBottomSheetContent(
                                bitmap = state.value.bitmap,
                                modifier = Modifier.fillMaxWidth()
                            )
                    },
                ) { paddingValues ->
                    PhotoChallengeAuthScreen(
                        modifier = Modifier.padding(paddingValues),
                        onAuthSuccess = {
                            setContent {
                                Box(
                                    modifier = Modifier
                                        .padding(paddingValues).padding(vertical = 60.dp)
                                ) {
                                    CameraPreview(
                                        controller = controller,
                                        modifier = Modifier.fillMaxSize().padding(vertical = 60.dp)
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.BottomCenter)
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = {
                                                coroutineScope.launch {
                                                    scaffoldState.bottomSheetState.expand()
                                                }
                                            },
                                            modifier = Modifier.offset(8.dp, 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.FilePresent,
                                                contentDescription = "Camera switch"
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                takePhoto(controller) { bitmap ->
                                                    viewModel.onTakenPhoto(bitmap = bitmap)
                                                }
                                            },
                                            modifier = Modifier
                                                .size(32.dp)
                                                .offset(8.dp, 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PhotoCamera,
                                                contentDescription = "Photo camera"
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                controller.cameraSelector =
                                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                                    } else {
                                                        CameraSelector.DEFAULT_BACK_CAMERA
                                                    }
                                            },
                                            modifier = Modifier.offset(8.dp, 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Cameraswitch,
                                                contentDescription = "Camera switch"
                                            )
                                        }
                                    }
                                }
                            }
                        })
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
                    onPhotoTaken.invoke(image.toBitmap())
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