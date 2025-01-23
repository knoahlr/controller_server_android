package com.example.server_2
import java.net.ServerSocket
import java.net.Socket
import java.net.NetworkInterface
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import kotlin.concurrent.thread
import LogViewModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

const val NUM_CLIENTS_MAX = 100
data class clientEntry(
    var socket: Socket? = null,
    var index: Int = 0,
    var active:Boolean = false,
)

class ServerActivity(val server_port: Int) {

    lateinit var serverSocket: ServerSocket
    var is_server_running = false
    val max_num_clients: Int = 100
    var client_count: Int = 0
    var second_count: Int = 0
    private val clients = mutableListOf<clientEntry>()
    private val logView: LogViewModel = LogViewModel()

    fun startServerOnPort() {
        serverSocket = ServerSocket(server_port)
        is_server_running = true
        if(true){
            Thread {
                // test log view
                test_log_thread_fn()
            }.start()
        }

    }

    fun listen(){
        var listening: Boolean = true
        while (true && listening) {
            var client_e: clientEntry = clientEntry(index = clients.size)
            try{
                client_e.socket = serverSocket.accept()
                client_e.active = true
                val clientSocket = requireNotNull(client_e.socket)
                synchronized(clients) {
                    if (clients.size < max_num_clients) {
                        try{
                            Thread {
                                handleClient(client_e)
                            }.start()
                            clients.add(client_e)
                            logView.postText("Client connected: ${clientSocket.inetAddress.hostAddress}\n")
                        }
                        catch(e:Exception){
                            logView.postText("Unable to start client handler thread${clientSocket.inetAddress.hostAddress}\n")
                        }
                    } else {
                        val rejected_sock = requireNotNull(client_e.socket)
                        rejected_sock.close()
                        listening = false
                        logView.postText("Rejected connection: ${clientSocket.inetAddress.hostAddress} (Server full)")
                    }
                }
            }catch(e: Exception){
                logView.postText("Exception caught: ${e.message}\n")
            }

        }
        logView.postText("Server Full: Closing listener.")
    }

    fun handleClient(clientEntry: clientEntry) {
        var clientConnected: Boolean = true
        val clientSocket = requireNotNull(clientEntry.socket)
        while(clientConnected) {
            clientSocket.use {  // Ensures socket is closed after use
                val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                val writer = PrintWriter(clientSocket.getOutputStream(), true)

                writer.println("Welcome to the server!")  // Send welcome message

                var message: String?
                while (reader.readLine().also { message = it } != null) {
                    logView.postText("${clientSocket.inetAddress.hostAddress}: $message\n")
                    /*if("DENJJ".toRegex(RegexOption.IGNORE_CASE).containsMatchIn(message))
                    {
                        clientConnected = false
                    }*/
                    writer.println("DENJJ: $message")  // Send response back to client
                }
                logView.postText("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
            }
            Thread.sleep(500)
        }
        logView.postText("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
        clients.remove(clientEntry)
    }

    fun test_log_thread_fn(){
        while(true) {
            if ((second_count % 5) == 0) {
                logView.postText("Fake client count: $second_count\n")
            }
            second_count = second_count + 1
            Thread.sleep(1000)
        }
    }
}

fun getLocalIPAddress(): String? {
    try {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        for (intf in interfaces) {
            val addresses = intf.inetAddresses
            for (addr in addresses) {
                if (!addr.isLoopbackAddress) {
                    val ipAddress = addr.hostAddress
                    if (ipAddress.contains(":").not()) { // Ignore IPv6 addresses
                        return ipAddress
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}