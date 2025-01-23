package com.example.server_2

import android.net.Uri
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.VideoView
import androidx.navigation.fragment.findNavController
import com.example.server_2.databinding.FragmentSecondBinding
import com.example.server_2.ServerActivity
import com.example.server_2.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    private val binding get() = _binding!!

    private lateinit var videoView: VideoView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        videoView = binding.videoViewBackground

        // Get the URI for the video in the raw folder
        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/raw/earth_from_space_2")

        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true  // Set looping
            mediaPlayer.setVolume(0f, 0f)  // Mute the video (optional)
            videoView.start()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ServerButton.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_SecondFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        videoView.start()  // Resume the video when fragment is visible
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()  // Pause video when fragment is not vsible
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}