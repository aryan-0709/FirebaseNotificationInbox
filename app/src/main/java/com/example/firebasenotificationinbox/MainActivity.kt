package com.example.firebasenotificationinbox

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.firebasenotificationinbox.data.local.NotificationDatabaseProvider
import com.example.firebasenotificationinbox.data.local.NotificationEntity
import com.example.firebasenotificationinbox.data.repository.NotificationRepository
import com.example.firebasenotificationinbox.notification.NotificationChannelManager
import com.example.firebasenotificationinbox.ui.inbox.NotificationNavGraph
import com.example.firebasenotificationinbox.ui.inbox.NotificationViewModel
import com.example.firebasenotificationinbox.ui.inbox.NotificationViewModelFactory
import com.example.firebasenotificationinbox.ui.theme.FirebaseNotificationInboxTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var repository: NotificationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = NotificationDatabaseProvider.getDatabase(
            applicationContext
        )

        val notificationDao = database.notificationDao()
        repository = NotificationRepository(notificationDao)

        val factory = NotificationViewModelFactory(repository)

        val notificationViewModel =
            ViewModelProvider(this, factory)[NotificationViewModel::class.java]

        NotificationChannelManager.createNotificationChannel(this)

        // Save data when app is opened by tapping an FCM notification
        saveNotificationFromIntent(intent)

        setContent {
            FirebaseNotificationInboxTheme {
                NotificationNavGraph(
                    viewModel = notificationViewModel
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        saveNotificationFromIntent(intent)
    }

    private fun saveNotificationFromIntent(intent: Intent?) {
        if (intent == null) return

        val extras = intent.extras ?: return

        val id = extras.get("id")?.toString() ?: return

        val title = extras.get("title")?.toString()
            ?: "New Notification"

        val description = extras.get("description")?.toString()
            ?: "No description available"

        val bannerImg = extras.get("bannerImg")?.toString()

        val timestamp = extras.get("timestamp")
            ?.toString()
            ?.toLongOrNull()
            ?.takeIf { it > 0L }
            ?: System.currentTimeMillis()

        val notification = NotificationEntity(
            id = id,
            title = title,
            description = description,
            bannerImageURL = bannerImg,
            timestamp = timestamp,
            isRead = false
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                repository.insertNotification(notification)
                Log.d("FCM_INTENT", "Notification saved: $id")
            } catch (exception: Exception) {
                Log.e(
                    "FCM_INTENT",
                    "Failed to save notification from intent",
                    exception
                )
            }
        }
    }
}



//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    NotificationPermissionButton() // permission Button Temporary
//                }


@Composable
fun NotificationPermissionButton() {
    val context = LocalContext.current

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        var hasPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            hasPermission = isGranted
        }

        if (!hasPermission) {
            Button(
                onClick = {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            ) {
                Text("Enable Notifications")
            }
        } else {
            Text("Notifications permission granted")
        }
    }
}
