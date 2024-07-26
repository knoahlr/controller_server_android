package com.example.server_1

class server {

    private val server_thread = Thread {
        println("Server Thread started")
        Thread.sleep(200)
        //create a GUI Textbox



    }

    init{
        server_thread.start()
    }
}