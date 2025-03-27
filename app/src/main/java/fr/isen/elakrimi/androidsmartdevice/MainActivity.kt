package fr.isen.elakrimi.androidsmartdevice

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.elakrimi.androidsmartdevice.screen.ScanActivity
import fr.isen.elakrimi.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidSmartDeviceTheme {
                FrontScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrontScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFBF6E4) // Fond beige
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo2),
                contentDescription = "Logo de l'application",
                modifier = Modifier.size(400.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Scannez les appareils BLE à proximité",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                color = Color(0xFFFFBAEC), // Rose clair
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                    val bluetoothAdapter = bluetoothManager.adapter

                    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                    when {
                        bluetoothAdapter == null -> {
                            showAlert(context, "Bluetooth non supporté", "Ce dispositif ne supporte pas le Bluetooth.")
                        }
                        !bluetoothAdapter.isEnabled -> {
                            showAlert(context, "Bluetooth désactivé", "Veuillez activer le Bluetooth pour continuer.")
                        }
                        !isLocationEnabled -> {
                            showAlert(context, "Localisation désactivée", "Veuillez activer la localisation pour scanner les appareils BLE.")
                        }
                        else -> {
                            context.startActivity(Intent(context, ScanActivity::class.java))
                        }
                    }
                },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBAEC)) // Bouton rose clair
            ) {
                Text(text = "Scan", color = Color.White)
            }
        }
    }
}

fun showAlert(context: Context, title: String, message: String) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK", null)
        .show()
}
