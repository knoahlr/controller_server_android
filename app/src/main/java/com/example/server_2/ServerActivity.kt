package com.example.server_2
import java.net.ServerSocket
import java.net.Socket
import java.net.NetworkInterface
import java.net.InetAddress
import java.util.*

import kotlin.concurrent.thread
import android.widget.TextView
import android.content.Context
import android.net.wifi.WifiManager

import androidx.lifecycle.ViewModelProvider
import com.example.server_2.databinding.FragmentSecondBinding
import LogViewModel

class ServerActivity(val server_port: Int) {

    lateinit var serverSocket: ServerSocket
    var is_server_running = false
    val max_num_clients: Int = 100
    var client_count: Int = 0
    lateinit var log_text_box: TextView
    lateinit var host_ip_text_view: TextView
    private val clients = mutableListOf<Socket>()
    private var server_page_binding: FragmentSecondBinding? = null
    private var logViewModel: LogViewModel = LogViewModel()

    fun startServerOnPort() {
        serverSocket = ServerSocket(server_port)
        is_server_running = true
        if(false){
            thread {
                // test log view
                test_log_thread_fn()
            }
        }

    }

    fun listen(){
        while (true) {
            val clientSocket: Socket = serverSocket.accept()
            synchronized(clients) {
                if (clients.size < max_num_clients) {
                    clients.add(clientSocket)
                    thread {
                        handleClient(clientSocket)
                    }
                    logViewModel.postText("Client connected: ${getLocalIPAddress()}\n")
                } else {
                    clientSocket.close()
                    logViewModel.postText("Rejected connection: ${getLocalIPAddress()} (Server full)")
                }
            }

        }
    }

    fun handleClient(clientSocket: Socket) {
        var clientConnected: Boolean = true
        while(clientConnected) {
            clientSocket.use {
                val input = it.getInputStream().bufferedReader().readLine()
                logViewModel.postText("${clientSocket.getInetAddress()}: $input")

                /* For now just  echo back responses before a handler is written */
                it.getOutputStream().bufferedWriter().apply {
                    write("DENJJ: $input\n")
                    flush()
                }

                if("DENJJ".toRegex(RegexOption.IGNORE_CASE).containsMatchIn(input))
                {
                    clientConnected = false
                }
            }
            Thread.sleep(10)
        }

        logViewModel.postText("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
        clients.remove(clientSocket)
    }

    fun test_log_thread_fn(){
        while(true) {
            if ((client_count % 500) == 0) {
                logViewModel.postText("Fake client count: $client_count\n")
            }
            client_count = client_count + 1
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