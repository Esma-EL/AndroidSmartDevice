package fr.isen.elakrimi.androidsmartdevice

import android.content.Intent // Importer la classe Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext // Importer LocalContext pour obtenir le contexte
import fr.isen.elakrimi.androidsmartdevice.ui.theme.PastelPink

@Composable
fun FrontScreen() {
    val context = LocalContext.current // Obtient le contexte


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFFBF6E4) // Fond du Scaffold
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
                color = Color(0xFFFFBAEC),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // Lancer l'activité ScanActivity
                    val intent = Intent(context, ScanActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFBAEC))
            ) {
                Text(text = "Scan", color = Color.White)
            }
        }
    }
}
