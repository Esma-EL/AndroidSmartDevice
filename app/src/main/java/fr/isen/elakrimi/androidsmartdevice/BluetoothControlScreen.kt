package fr.isen.elakrimi.androidsmartdevice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothControlScreen(
    name: String,
    address: String,
    rssi: Int,
    onBack: () -> Unit,
    onConnectClick: () -> Unit,
    connectionStatus: String,
    isConnected: Boolean,
    ledStates: List<Boolean>,
    onLedToggle: (Int) -> Unit,
    isSubscribedButton1: Boolean,
    isSubscribedButton3: Boolean,
    onSubscribeToggleButton1: (Boolean) -> Unit,
    onSubscribeToggleButton3: (Boolean) -> Unit,
    counterButton1: Int,
    counterButton3: Int,
    onResetCounter: () -> Unit
) {
    val backgroundColor = Color(0xFFFBF6E4)
    val ledColors = listOf(
        Color(0xFF1976D2), // LED 1 - Bleu
        Color(0xFF4CAF50), // LED 2 - Vert
        Color(0xFFF44336)  // LED 3 - Rouge
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AndroidSmartDevice") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(backgroundColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isConnected) {
                Text("Périphérique détecté", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFBAEC))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Nom : $name", fontSize = 16.sp)
                Text("Adresse : $address", fontSize = 14.sp, color = Color.Gray)
                Text("RSSI : $rssi dBm", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Text(connectionStatus, fontSize = 14.sp, color = Color(0xFF000000))
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onConnectClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBAEC))
                ) {
                    Text("Se connecter", color = Color.White, fontSize = 16.sp)
                }
            } else {
                Text("Votre Sapin De Noël", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFBAEC))
                Spacer(modifier = Modifier.height(24.dp))
                Text("Vos Guirlandes", fontSize = 16.sp, fontWeight = FontWeight.Medium)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ledStates.forEachIndexed { index, isOn ->
                        val color = ledColors.getOrNull(index) ?: Color.Gray
                        Button(
                            onClick = { onLedToggle(index) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isOn) color else Color.LightGray
                            ),
                            modifier = Modifier
                                .height(64.dp)
                                .width(100.dp)
                        ) {
                            Text("LED ${index + 1}", color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isSubscribedButton3,
                        onCheckedChange = onSubscribeToggleButton3
                    )
                    Text("Abonnement notif bouton 1")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isSubscribedButton1,
                        onCheckedChange = onSubscribeToggleButton1
                    )
                    Text("Abonnement notif bouton 3")
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Compteur bouton 1 : $counterButton3", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Compteur bouton 3 : $counterButton1", fontSize = 16.sp)

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onResetCounter) {
                    Text("Réinitialiser les compteurs")
                }
            }
        }
    }
}
