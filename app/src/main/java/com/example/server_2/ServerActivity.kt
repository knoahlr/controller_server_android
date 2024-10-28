package com.example.server_2
import androidx.annotation.Nullable
import java.net.ServerSocket
import java.net.Socket
import java.net.InetAddress
import kotlin.concurrent.thread

class ServerActivity(val server_port: Int) {

    lateinit var serverSocket: ServerSocket
    var is_server_running = false
    val max_num_clients: Int = 100
    var client_count: Int = 0
    private val clients = mutableListOf<Socket>()

    fun runServer() {
        serverSocket = ServerSocket(server_port)
        //println("Server is running on port $server_connection_port")
        is_server_running = true
    }

    fun listen(){
        while (true) {
            val clientSocket = serverSocket.accept()
            synchronized(clients) {
                if (clients.size < max_num_clients) {
                    clients.add(clientSocket)
                    thread { handleClient(clientSocket) }
                    println("Client connected: ${clientSocket.inetAddress.hostAddress}")
                } else {
                    clientSocket.close()
                    println("Rejected connection: ${clientSocket.inetAddress.hostAddress} (Server full)")
                }
            }
        }
    }

    private fun handleClient(clientSocket: Socket) {
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
            println("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
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
