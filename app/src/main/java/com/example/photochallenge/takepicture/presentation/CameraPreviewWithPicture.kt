package com.example.photochallenge.takepicture.presentation

import androidx.camera.core.CameraSelector
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CameraPreviewWithPicture(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    onClickToTakePhoto: () -> Unit,
) {
    Box(
        modifier = modifier.padding(vertical = 60.dp)
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 60.dp)
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
                    onClickToTakePhoto.invoke()
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