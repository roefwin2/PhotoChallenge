package com.example.photochallenge.takepicture.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.photochallenge.R
import com.example.photochallenge.activity.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CameraPreviewWithPicture(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = koinViewModel(),
    onClickToTakePhoto: () -> Unit,
) {
    val selectedEmoji = mainViewModel.state.value.selectedEmoji ?: R.drawable.angry1
    Box(
        modifier = modifier.padding(vertical = 60.dp)
    ) {
        Text(
            text = "Imiter l'image à droite",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CameraPreview(
            controller = controller,
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(16.dp)
                )
        )
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(100.dp)
                .align(Alignment.TopEnd)
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            Image(
                painter = painterResource(selectedEmoji),
                contentDescription = "Image superposée",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
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