package com.example.server_2

import LogViewModel
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.server_2.databinding.FragmentSecondBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    lateinit var connect_button: Button
    lateinit var root_view: View
    lateinit var logTextView: TextView
    //private var logViewModel: LogViewModel = LogViewModel()
    private val logViewModel: LogViewModel by activityViewModels()
    var connect_button_is_green: Boolean = false

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
        binding.homeButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_HomeFragment)
        }

        // Optional: Add long press listener for additional functionality
        binding.homeButton.setOnLongClickListener {
            Snackbar.make(view, "Home Button Long Pressed", Snackbar.LENGTH_SHORT).show()
            true
        }

        logViewModel.text.observe(viewLifecycleOwner) { newText ->
            logTextView.text = newText
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