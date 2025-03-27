package fr.isen.elakrimi.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import fr.isen.elakrimi.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme


class BluetoothControlActivity : ComponentActivity() {

    private var gatt: BluetoothGatt? = null
    private var ledChar: BluetoothGattCharacteristic? = null
    private var notifCharButton1: BluetoothGattCharacteristic? = null
    private var notifCharButton3: BluetoothGattCharacteristic? = null

    private val ledStates = mutableStateListOf(false, false, false)
    private val connectionState = mutableStateOf("Appuyez sur le bouton pour vous connecter")

    private val counterButton1 = mutableStateOf(0)
    private val counterButton3 = mutableStateOf(0)

    private val isSubscribedButton1 = mutableStateOf(false)
    private val isSubscribedButton3 = mutableStateOf(false)

    private var skipNextNotification1 = false
    private var skipNextNotification3 = false

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = intent.getStringExtra("name") ?: "Appareil inconnu"
        val address = intent.getStringExtra("address") ?: "N/A"
        val rssi = intent.getIntExtra("rssi", 0)

        setContent {
            AndroidSmartDeviceTheme {
                BluetoothControlScreen(
                    name = name,
                    address = address,
                    rssi = rssi,
                    onBack = { finish() },
                    onConnectClick = { connectToDevice(address, name) },
                    connectionStatus = connectionState.value,
                    isConnected = connectionState.value.contains("Connecté"),
                    ledStates = ledStates,
                    onLedToggle = { toggleLed(it) },
                    isSubscribedButton1 = isSubscribedButton1.value,
                    isSubscribedButton3 = isSubscribedButton3.value,
                    onSubscribeToggleButton1 = { toggleNotificationsFor(notifCharButton1, it) },
                    onSubscribeToggleButton3 = { toggleNotificationsFor(notifCharButton3, it) },
                    counterButton1 = counterButton1.value,
                    counterButton3 = counterButton3.value,
                    onResetCounter = {
                        counterButton1.value = 0
                        counterButton3.value = 0
                    }
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(address: String, name: String) {
        connectionState.value = "Connexion BLE en cours..."
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val device = bluetoothAdapter.getRemoteDevice(address)

        gatt = device.connectGatt(this, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    connectionState.value = " Connecté à $name"
                    gatt.discoverServices()
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    connectionState.value = " Déconnecté"
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun toggleLed(index: Int) {
        val char = ledChar ?: return
        val alreadyOn = ledStates[index]
        for (i in ledStates.indices) {
            ledStates[i] = false
        }
        val valueToSend = if (alreadyOn) 0x00 else (index + 1)
        char.value = byteArrayOf(valueToSend.toByte())
        gatt?.writeCharacteristic(char)
        if (!alreadyOn) {
            ledStates[index] = true
        }
    }

    @SuppressLint("MissingPermission")
    private fun toggleNotificationsFor(
        characteristic: BluetoothGattCharacteristic?,
        enable: Boolean
    ) {
        if (characteristic == null) return

        gatt?.setCharacteristicNotification(characteristic, enable)

        val descriptor = characteristic.getDescriptor(
            characteristic.descriptors.firstOrNull()?.uuid ?: return
        )
        descriptor.value = if (enable)
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        else
            BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE

        gatt?.writeDescriptor(descriptor)
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        gatt?.close()
    }
}
