package com.example.photochallenge.feature.takepicture.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Videocam
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
    onVideoCallClick: () -> Unit,
    onClickToTakePhoto: () -> Unit,
) {
    val selectedEmoji = mainViewModel.state.value.selectedEmoji ?: R.drawable.angry1
    Box(
        modifier = modifier.padding(vertical = 60.dp)
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(16.dp)
                )
        )
        ImageInstruction(
            modifier = Modifier
                .align(Alignment.TopEnd),
            emoji = selectedEmoji
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
                    onVideoCallClick.invoke()
                },
                modifier = Modifier.offset(8.dp, 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Videocam,
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

@Composable
fun ImageInstruction(
    emoji: Int,
    modifier: Modifier = Modifier,
    message: String = "Imiter l'image en bas"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .size(100.dp)
                .border(
                    width = 4.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(emoji),
                contentDescription = "Image superposée",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}