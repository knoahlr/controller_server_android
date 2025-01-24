package com.example.server_2

import LogViewModel
import HostIpViewModel

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels

import android.widget.TextView
import androidx.navigation.fragment.findNavController

import com.example.server_2.databinding.FragmentSecondBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    lateinit var logTextView: TextView
    lateinit var host_ip: TextView

    private val logViewModel:LogViewModel by activityViewModels()

    private val hostIpViewModel: HostIpViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logTextView  = binding.contStateTextView
        logTextView.movementMethod = ScrollingMovementMethod.getInstance()
        logTextView.isVerticalScrollBarEnabled = true
        logTextView.isHorizontalScrollBarEnabled = false
        binding.homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_HomeFragment)
        }
        binding.linksButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment)
        }

        // Optional: Add long press listener for additional functionality
        binding.homeButton.setOnLongClickListener {
            Snackbar.make(view, "Home Button Long Pressed", Snackbar.LENGTH_SHORT).show()
            true
        }

        hostIpViewModel._ip.observe(viewLifecycleOwner) { ipText ->
            println("Observe: $ipText")
            host_ip.append(ipText)
        }

        logViewModel._text.observe(viewLifecycleOwner) { newText ->
            println("Observe: $newText")
            logTextView.append(newText)
        }
    }

    // To change button position programmatically if needed
    private fun setHomeButtonPosition(position: HomeButtonPosition) {
        view?.findViewById<FloatingActionButton>(R.id.homeButton)?.let { button ->
            val params = button.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams

            when (position) {
                HomeButtonPosition.BOTTOM_RIGHT -> {
                    params.endToEnd = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
                    params.startToStart = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
                }
                HomeButtonPosition.BOTTOM_LEFT -> {
                    params.startToStart = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
                    params.endToEnd = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.UNSET
                }
            }

            button.layoutParams = params
        }
    }

    enum class HomeButtonPosition {
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}