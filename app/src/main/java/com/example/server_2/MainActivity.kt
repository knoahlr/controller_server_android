package com.example.server_2
import LogViewModel
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.content.pm.PackageManager
import android.content.Intent
import androidx.navigation.ui.AppBarConfiguration
import androidx.activity.viewModels
import androidx.navigation.NavController
import com.example.server_2.databinding.ActivityMainBinding
import kotlin.concurrent.thread
import android.bluetooth.BluetoothAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var server: ServerService
    lateinit var nav_control: NavController
    private val logViewModel: LogViewModel by viewModels()

    private val bluetoothPermissions = arrayOf(
        android.Manifest.permission.BLUETOOTH_SCAN,
        android.Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ServerActivity with the desired port
        server = ServerService.get_server_port(port = 8080)

        // Start the server in a separate thread to avoid blocking the main UI thread
        thread {
            server.startServerOnPort()  // Initialize the server
        }
    }

    private fun checkBluetoothPermissions(): Boolean {
        bluetoothPermissions.forEach {
            if (checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun requestBluetoothPermissions() {
        requestPermissions(bluetoothPermissions, requestCodeBluetooth)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeBluetooth) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Log.d("BluetoothPermission", "All Bluetooth permissions granted")
                enableBluetooth()
            } else {
                Log.e("BluetoothPermission", "Bluetooth permissions denied")
            }
        }
    }

    private fun enableBluetooth() {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Log.e("Bluetooth", "Bluetooth is not supported on this device")
        } else if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, requestCodeBluetooth)
        }
    }

}
