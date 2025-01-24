package com.example.server_2
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.fragment.app.activityViewModels
import androidx.core.app.NotificationCompat
import java.net.ServerSocket
import java.net.Socket
import java.net.NetworkInterface
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import LogViewModel
import HostIpViewModel

const val NUM_CLIENTS_MAX = 100
const val SERVER_NOTIFICATION_ID = 1
const val SERVER_CHANNEL_ID = "server_service_channel"

data class ClientEntry(
    var socket: Socket? = null,
    var index: Int = 0,
    var active: Boolean = false,
)

class ServerService(val port: Int) : Service() {

    lateinit var serverSocket: ServerSocket
    private val server_port = port
    var is_server_running = false
    val max_num_clients: Int = 100
    private val clients = mutableListOf<ClientEntry>()
    private lateinit var log_view:LogViewModel
    private lateinit var client_ip_view: HostIpViewModel

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val serverPort = intent?.getIntExtra("PORT", server_port)
        startServerOnPort()
        return START_STICKY
    }

    private fun startForegroundService() {
        createNotificationChannel()
        val notification: Notification = NotificationCompat.Builder(this, SERVER_CHANNEL_ID)
            .setContentTitle("Server Running")
            .setContentText("Server is running in the background")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()

        startForeground(SERVER_NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                SERVER_CHANNEL_ID,
                "Server Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }


    fun startServerOnPort() {
        log_view = LogViewModel()
        client_ip_view = HostIpViewModel()

        Thread {
            try {
                serverSocket = ServerSocket(server_port)
                is_server_running = true
                logMessage("Server started on port $server_port")

                while (is_server_running) {
                    val clientEntry = ClientEntry(index = clients.size)
                    clientEntry.socket = serverSocket.accept()
                    clientEntry.active = true
                    synchronized(clients) {
                        if (clients.size < max_num_clients) {
                            Thread { handleClient(clientEntry) }.start()
                            clients.add(clientEntry)
                                logMessage("Client connected: ${clientEntry.socket?.inetAddress?.hostAddress}")
                                client_ip_view.postText("${clientEntry.socket?.inetAddress?.hostAddress}")
                        } else {
                            clientEntry.socket?.close()
                            logMessage("Rejected connection: Server full")
                        }
                    }
                }
            } catch (e: Exception) {
                logMessage("Server exception: ${e.message}")
            }
        }.start()
    }

    private fun handleClient(clientEntry: ClientEntry) {
        val clientSocket = clientEntry.socket ?: return
        try {
            val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            val writer = PrintWriter(clientSocket.getOutputStream(), true)
            writer.println("Welcome to the server!")

            var message: String?
            while (reader.readLine().also { message = it } != null) {
                logMessage("${clientSocket.inetAddress.hostAddress}: $message")
                writer.println("DENJJ: $message")
            }
        } catch (e: Exception) {
            logMessage("Client handler error: ${e.message}")
        } finally {
            clients.remove(clientEntry)
            clientSocket.close()
            logMessage("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
        }
    }

    private fun logMessage(message: String) {
        // Log to console, or use Android logs
        log_view.postText(message)
        println(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        is_server_running = false
        serverSocket.close()
        logMessage("Server stopped")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
