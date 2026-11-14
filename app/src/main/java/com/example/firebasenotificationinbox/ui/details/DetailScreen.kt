package com.example.firebasenotificationinbox.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.firebasenotificationinbox.ui.inbox.NotificationViewModel
import java.text.DateFormat
import java.util.Date

@Composable
fun DetailsScreen(
    notificationId: String,
    viewModel: NotificationViewModel
) {
    val notifications by viewModel.notifications.collectAsState()

    val notification = notifications.find {
        it.id == notificationId
    }

    LaunchedEffect(notificationId) {
        viewModel.markAsRead(notificationId)
    }

    if (notification == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Notification not found")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (notification.bannerImageURL.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Image unavailable")
                }
            } else {
                SubcomposeAsyncImage(
                    model = notification.bannerImageURL,
                    contentDescription = "Notification banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Image unavailable")
                        }
                    }
                )
            }

            Text(
                text = notification.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = notification.description,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = DateFormat.getDateTimeInstance().format(
                    Date(notification.timestamp)
                ),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
