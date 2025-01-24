package com.example.server_2

import LogViewModel
import HostIpViewModel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.server_2.databinding.FragmentSecondBinding

import com.example.server_2.databinding.FragmentThirdBinding

/**
 * A simple [Fragment] subclass as the third destination in the navigation.
 */
class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private lateinit var containerLayout: LinearLayout
    private lateinit var addButton: Button
    private var textCount = 1

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root

        // Initialize views
        containerLayout = view.findViewById(R.id.textContainer)
        addButton = view.findViewById(R.id.btnAdd)

        // Set click listener on the add button
        addButton.setOnClickListener {
            addTextView()
        }

        return view
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
}