import android.content.Context
import android.util.Log
import java.util.*
import java.io.IOException
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter

class BluetoothDeviceReceiver(private val onDeviceFound: (BluetoothDevice) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == BluetoothDevice.ACTION_FOUND) {
            val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            if (device != null) {
                onDeviceFound(device)
            }
        }
    }

    companion object {
        val filter: IntentFilter
            get() = IntentFilter(BluetoothDevice.ACTION_FOUND)
    }
}

class BluetoothConnectionManager(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var isConnected = false
    private var device: BluetoothDevice? = null

    companion object {
        private const val TAG = "BluetoothManager"
        private val APP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Standard SPP UUID
    }

    /**
     * Check if Bluetooth is supported on the device.
     */
    fun isBluetoothSupported(): Boolean {
        return bluetoothAdapter != null
    }

    /**
     * Enable Bluetooth if it is not already enabled.
     */
    fun enableBluetooth() {
        if (bluetoothAdapter?.isEnabled == false) {
            bluetoothAdapter.enable()
        }
    }

    /**
     * Start scanning for Bluetooth devices.
     */
    fun startScanning(callback: (BluetoothDevice) -> Unit) {
        val receiver = BluetoothDeviceReceiver { device ->
            Log.d(TAG, "Discovered device: ${device.name} (${device.address})")
            callback(device)
        }
        context.registerReceiver(receiver, BluetoothDeviceReceiver.filter)
        bluetoothAdapter?.startDiscovery()
    }

    /**
     * Stop scanning for Bluetooth devices.
     */
    fun stopScanning() {
        bluetoothAdapter?.cancelDiscovery()
    }

    /**
     * Connect to a Bluetooth device.
     */
    fun connectToDevice(device: BluetoothDevice): Boolean {
        stopScanning()
        this.device = device
        return try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(APP_UUID)
            bluetoothSocket?.connect()
            isConnected = true
            Log.d(TAG, "Connected to ${device.name} (${device.address})")
            true
        } catch (e: IOException) {
            Log.e(TAG, "Failed to connect to ${device.name} (${device.address})", e)
            closeConnection()
            false
        }
    }

    /**
     * Send data to the connected Bluetooth device.
     */
    fun sendData(data: String) {
        if (isConnected) {
            try {
                bluetoothSocket?.outputStream?.write(data.toByteArray())
                Log.d(TAG, "Data sent: $data")
            } catch (e: IOException) {
                Log.e(TAG, "Failed to send data", e)
            }
        } else {
            Log.e(TAG, "Not connected to any device")
        }
    }

    /**
     * Close the connection to the Bluetooth device.
     */
    fun closeConnection() {
        try {
            bluetoothSocket?.close()
            bluetoothSocket = null
            isConnected = false
            Log.d(TAG, "Connection closed")
        } catch (e: IOException) {
            Log.e(TAG, "Failed to close connection", e)
        }
    }

}