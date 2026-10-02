package com.bancocloud.traza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bancocloud.traza.app.TrazaAdminApp
import com.bancocloud.traza.app.TrazaApplication
import com.bancocloud.traza.core.designsystem.theme.TrazaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as TrazaApplication).container
        setContent {
            TrazaTheme {
                TrazaAdminApp(container)
            }
        }
    }
}
