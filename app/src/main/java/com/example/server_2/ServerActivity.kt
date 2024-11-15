package com.example.server_2
import androidx.annotation.Nullable
import java.net.ServerSocket
import java.net.Socket
import java.net.InetAddress
import kotlin.concurrent.thread
import android.widget.TextView
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
        //logViewModel.updateText("Server is running.\n")
        //server_page_binding = FragmentSecondBinding.inflate(inflater, container, false)
        //log_text_box = server_page_binding.cont_state_text_view
    }

    fun listen(){
        while (true) {
            val clientSocket = serverSocket.accept()
            synchronized(clients) {
                if (clients.size < max_num_clients) {
                    clients.add(clientSocket)
                    thread {
                        handleClient(clientSocket)
                    }
                    logViewModel.updateText("Client connected: ${clientSocket.inetAddress.hostAddress}\n")
                } else {
                    clientSocket.close()
                    logViewModel.updateText("Rejected connection: ${clientSocket.inetAddress.hostAddress} (Server full)")
                }
            }
            if(client_count % 500 == 0) { logViewModel.updateText("Fake client count: ${client_count}")}
            client_count = client_count + 1
        }
    }

    fun handleClient(clientSocket: Socket) {
        while(true) {
            clientSocket.use {
                val input = it.getInputStream().bufferedReader().readLine()
                println("Received: $input from ${it.inetAddress.hostAddress}")

                it.getOutputStream().bufferedWriter().apply {
                    write("Message received\n")
                    flush()
                }
            }
        }
        synchronized(clients) {
            clients.remove(clientSocket)
            logViewModel.updateText("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
        }
    }

    fun stop() {
        synchronized(clients) {
            clients.forEach { it.close() }
            clients.clear()
        }
        serverSocket.close()
        println("Server stopped")
    }
}
