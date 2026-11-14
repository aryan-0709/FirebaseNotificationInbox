package com.example.firebasenotificationinbox.ui.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import java.text.DateFormat
import java.util.Date

@Composable
fun InboxScreen(
    viewModel: NotificationViewModel,
    onNotificationClick: (String) -> Unit
) {
    val notifications by viewModel.notifications.collectAsState()

    var showUnreadOnly by remember {
        mutableStateOf(false)
    }

    // Calculate unread notifications
    val unreadCount = notifications.count {
        !it.isRead
    }

    // Filter notifications
    val displayedNotifications = if (showUnreadOnly) {
        notifications.filter { !it.isRead }
    } else {
        notifications
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),

        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 20.dp
        ),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Header
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Notification Center",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Stay updated with what's happening",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                NotificationPermissionSection()  // shows permission box

            }
        }



        // Summary Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // All Notifications Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(125.dp)
                        .clickable {
                            showUnreadOnly = false
                        },

                    colors = CardDefaults.cardColors(
                        containerColor = if (!showUnreadOnly) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "All Notifications",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = notifications.size.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Total received",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Unread Notifications Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(125.dp)
                        .clickable {
                            showUnreadOnly = true
                        },

                    colors = CardDefaults.cardColors(
                        containerColor = if (showUnreadOnly) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Unread",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = unreadCount.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Need attention",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Notification List Heading
        item {
            Text(
                text = if (showUnreadOnly) {
                    "Unread Notifications"
                } else {
                    "Recent Notifications"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Empty State
        if (displayedNotifications.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (showUnreadOnly) {
                            "You're all caught up!"
                        } else {
                            "No notifications yet"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Notification List
        items(
            items = displayedNotifications,
            key = { notification -> notification.id }
        ) { notification ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNotificationClick(notification.id)
                    },

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    // Banner Image
                    if (notification.bannerImageURL.isNullOrBlank()) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Image unavailable",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                    } else {

                        SubcomposeAsyncImage(
                            model = notification.bannerImageURL,
                            contentDescription = "Notification banner",

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),

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

                    // Title and Unread Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = notification.title,

                            style = MaterialTheme.typography.titleMedium,

                            fontWeight = if (!notification.isRead) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            },

                            modifier = Modifier.weight(1f)
                        )

                        if (!notification.isRead) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }

                    // Description
                    Text(
                        text = notification.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // Timestamp
                    Text(
                        text = DateFormat.getDateTimeInstance().format(
                            Date(notification.timestamp)
                        ),

                        style = MaterialTheme.typography.labelSmall,

                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

