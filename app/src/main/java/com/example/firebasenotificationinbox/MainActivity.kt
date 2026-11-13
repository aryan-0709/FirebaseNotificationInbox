package com.example.firebasenotificationinbox

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.firebasenotificationinbox.ui.theme.FirebaseNotificationInboxTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task->
                if (task.isSuccessful)
                {
                    Log.d("FCM_TOKEN",task.result)
                }else
                {
                    Log.d("FCM_TOKEN","Token fetch failed",task.exception)
                }
            }

    }
}

