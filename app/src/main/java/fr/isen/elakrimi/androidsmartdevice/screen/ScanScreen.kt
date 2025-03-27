package fr.isen.elakrimi.androidsmartdevice.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.platform.LocalContext
import fr.isen.elakrimi.androidsmartdevice.R
import androidx.compose.ui.text.font.FontWeight



data class BLEDevice(val name: String, val address: String, val rssi: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    devices: List<BLEDevice>,
    isScanning: Boolean,
    remainingTime: Int,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onBack: () -> Unit,
    onDeviceClick: (BLEDevice) -> Unit
) {
    val context = LocalContext.current
    val backgroundColor = Color(0xFFFBF6E4) // Background color as defined in the second package

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan BLE") },
                navigationIcon = {
                    IconButton(onClick = { (context as? android.app.Activity)?.finish() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor) // Apply the background color
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
                        if (isScanning) onStopScan() else onStartScan()
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isScanning) "Scan en cours..." else "Scan arrêté",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (devices.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 60.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_start), //
                            contentDescription = "Bluetooth",
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Aucun appareil détecté", fontSize = 16.sp, color = Color.Black)

                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(devices) { device ->
                        BLEDeviceItem(device) {
                            onDeviceClick(device)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BLEDeviceItem(device: BLEDevice, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xFFFFBAEC),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "${device.rssi}dB",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = device.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
                Text(
                    text = device.address,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
