package com.example.server_2
import androidx.annotation.Nullable
import java.net.ServerSocket
import java.net.Socket
import java.net.InetAddress
import kotlin.concurrent.thread

class ServerActivity {

    lateinit var serverSocket: ServerSocket
    var server_connection_state = false
    var server_connection_port = 9999
    val max_num_clients: Int = 100
    var client_count: Int = 0
    private val clients = mutableListOf<Socket>()

    fun runServer() {
        serverSocket = ServerSocket(server_connection_port)
        println("Server is running on port $server_connection_port")

        while (true) {
            /* start */
        }

        serverSocket.close()
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

    /* private fun handleClient(clientSocket: Socket) {
        try {
            clientSocket.use {
                val input = it.getInputStream().bufferedReader().readLine()
                println("Received: $input from ${it.inetAddress.hostAddress}")

                it.getOutputStream().bufferedWriter().apply {
                    write("Message received\n")
                    flush()
                }
            }
        } catch (e: Exception) {
            println("Error handling client: ${clientSocket.inetAddress.hostAddress}")
        } finally {
            synchronized(clients) {
                clients.remove(clientSocket)
                println("Client disconnected: ${clientSocket.inetAddress.hostAddress}")
            }
        }
    } */

    fun stop() {
        synchronized(clients) {
            clients.forEach { it.close() }
            clients.clear()
        }
        println("Server stopped")
    }
}
