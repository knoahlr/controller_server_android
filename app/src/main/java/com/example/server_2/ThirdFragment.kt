package com.example.server_2
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.EditText
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import  com.example.server_2.ServerService
import androidx.navigation.fragment.findNavController
import com.example.server_2.databinding.FragmentSecondBinding

import com.example.server_2.databinding.FragmentThirdBinding

/**
 * A simple [Fragment] subclass as the third destination in the navigation.
 */
class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private lateinit var containerLayout: LinearLayout
    private lateinit var add_link_button: Button
    private lateinit var client_scroll_1: Spinner
    private lateinit var client_scroll_2: Spinner
    private var textCount = 1
    private lateinit var third_fragment_inflater: LayoutInflater

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        third_fragment_inflater = inflater
        val view = third_fragment_inflater.inflate(R.layout.fragment_third, container, false)
        _binding = FragmentThirdBinding.inflate(inflater, container, false)

        // Set click listener on the add link button
        binding.addLink.setOnClickListener {
            get_link_info()
        }
        return binding.root
    }

    // Function to add a new TextView dynamically
    private fun addTextView() {
        val newTextView = TextView(requireContext()).apply {
            text = "Text Entry $textCount"
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }
        containerLayout.addView(newTextView)
        textCount++
    }


    private fun get_link_info() {
        // Inflate the popup layout
        val popupView = third_fragment_inflater.inflate(R.layout.popup_input, null)
        // Create AlertDialog with the custom layout
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(popupView)
            .setCancelable(true)
            .create()

        // List of dropdown options
        val options = ServerService.clients.map{"${it.socket?.inetAddress?.hostAddress}"}
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, options)

        // Find views inside the popup
        client_scroll_1 = popupView.findViewById<Spinner>(R.id.link_1_spinner)
        client_scroll_2 = popupView.findViewById<Spinner>(R.id.link_2_spinner)
        client_scroll_1.adapter = adapter
        client_scroll_2.adapter = adapter

        val btnSubmit = popupView.findViewById<Button>(R.id.btnSubmit)

        //Handle submit button click
        btnSubmit.setOnClickListener {
            val link_1_ip = client_scroll_1.selectedItem.toString()
            val link_2_ip = client_scroll_2.selectedItem.toString()

            if (link_1_ip.isEmpty() || link_2_ip.isEmpty()) {
                // log stat
            } else {
                // Handle the entered data (e.g., show it in a toast)
                //create_link(link_1_ip, link_2_ip)
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }
}

private fun create_link(ip_1: String, ip_2: String) {
    // create links
}