package fr.isen.elakrimi.androidsmartdevice

import android.os.Bundle
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Importer Color ici
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import fr.isen.elakrimi.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme
import androidx.compose.foundation.background

class ScanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth non disponible sur ce téléphone", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, nothing more to do.
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT), 2)
            }
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }
        setContent {
            AndroidSmartDeviceTheme {
                ScanScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen() {
    val activity = LocalActivity.current
    var isScanning by remember { mutableStateOf(false) }
    var devices by remember { mutableStateOf(listOf("Aucun appareil")) }

    val backgroundColor = Color(0xFFFBF6E4) // Couleur d'arrière-plan

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan BLE") },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)  // Appliquer la nouvelle couleur de fond
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Scan des appareils BLE",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = if (isScanning) R.drawable.ic_stop else R.drawable.ic_start),
                contentDescription = "Scan Icon",
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        isScanning = !isScanning
                        devices = if (isScanning) {
                            listOf("BLE Device 1", "BLE Device 2")
                        } else {
                            listOf("Aucun appareil")
                        }
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isScanning) "Scan en cours..." else "Scan arrêté",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn {
                items(devices) { device ->
                    Text(
                        text = device,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

