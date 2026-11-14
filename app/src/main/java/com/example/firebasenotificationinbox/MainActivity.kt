package com.example.firebasenotificationinbox

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import com.example.firebasenotificationinbox.data.local.NotificationDatabaseProvider
import com.example.firebasenotificationinbox.data.repository.NotificationRepository
import com.example.firebasenotificationinbox.notification.NotificationChannelManager
import com.example.firebasenotificationinbox.ui.inbox.NotificationNavGraph
import com.example.firebasenotificationinbox.ui.inbox.NotificationViewModel
import com.example.firebasenotificationinbox.ui.inbox.NotificationViewModelFactory
import com.example.firebasenotificationinbox.ui.theme.FirebaseNotificationInboxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //   Database instance
        val database = NotificationDatabaseProvider.getDatabase(
            applicationContext
        )

        //   DAO
        val notificationDao = database.notificationDao()

        //  Create Repository
        val repository = NotificationRepository(notificationDao)

        //  Create ViewModel Factory
        val factory = NotificationViewModelFactory(repository)

        //  Create ViewModel
        val notificationViewModel =
            ViewModelProvider(this, factory)[NotificationViewModel::class.java]

        // Create Notification Channel
        NotificationChannelManager.createNotificationChannel(this)


        setContent {
            FirebaseNotificationInboxTheme {

                NotificationNavGraph(
                    viewModel = notificationViewModel
                )



//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    NotificationPermissionButton() // permission Button Temporary
//                }
            }
        }



    }
}

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
