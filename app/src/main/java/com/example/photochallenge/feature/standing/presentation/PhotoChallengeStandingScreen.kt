package com.example.photochallenge.feature.standing.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.photochallenge.feature.authentification.domain.models.PhotoChallengeUser
import org.koin.androidx.compose.koinViewModel

@Composable
fun PhotoChallengeStandingScreen(
    modifier: Modifier = Modifier,
    viewModel: PhotoChallengeStandingViewModel = koinViewModel()
) {
    val state = viewModel.standingState.collectAsState().value
    if (state.isSuccess) {
        val users = state.getOrThrow().sortedByDescending { it.currentPictureUri?.votingCount }
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "Vainqueur du dernier challenge",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(24.dp)
            )
            TopThreeSection(users.take(3))
            UsersList(users.drop(3))
        }
    }
}

@Composable
fun TopThreeSection(topUsers: List<PhotoChallengeUser>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            TopUserItem(
                user = topUsers.getOrNull(1),
                modifier = Modifier.weight(1f),
                size = 100.dp,
                medalColor = Color(0xFFC0C0C0)
            )

            TopUserItem(
                user = topUsers.getOrNull(0),
                modifier = Modifier.weight(1f),
                size = 120.dp,
                medalColor = Color(0xFFFFD700)
            )

            TopUserItem(
                user = topUsers.getOrNull(2),
                modifier = Modifier.weight(1f),
                size = 80.dp,
                medalColor = Color(0xFFCD7F32)
            )
        }
    }
}

@Composable
fun TopUserItem(
    user: PhotoChallengeUser?,
    modifier: Modifier = Modifier,
    size: Dp,
    medalColor: Color
) {
    if (user == null) return

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(
                model = user.currentPictureUri?.bitmap,
                contentDescription = null,
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = medalColor,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = user.firstname,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = "${user.currentPictureUri?.votingCount} pts",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun UsersList(users: List<PhotoChallengeUser>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(users) { index, user ->
            UserListItem(
                user = user,
                position = index + 4,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun UserListItem(
    user: PhotoChallengeUser,
    position: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$position",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(30.dp)
            )

            AsyncImage(
                model = user.currentPictureUri?.bitmap,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = user.lastname,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${user.currentPictureUri?.votingCount} points",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}