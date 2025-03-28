package fr.isen.elakrimi.androidsmartdevice

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.isen.elakrimi.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidSmartDeviceTheme {
                FrontScreen() // Appel de la fonction FrontScreen depuis front.kt
            }
        }
    }
}
