package com.example.standardapp.ui.media

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.standardapp.R
import com.example.standardapp.databinding.FragmentMediaBinding

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null
    private var videoSurface: Surface? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startCameraPreview()
        } else {
            _binding?.cameraStatusText?.text = "Camera permission: denied"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.testImage.setImageResource(R.drawable.test_image)
        binding.videoTexture.surfaceTextureListener = videoSurfaceListener
        binding.previewView.setOnClickListener {
            requestCameraPermission()
        }
        updateCameraStatus()
        if (hasCameraPermission()) {
            startCameraPreview()
        }
    }

    private val videoSurfaceListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(
            surfaceTexture: SurfaceTexture,
            width: Int,
            height: Int
        ) {
            prepareVideo(surfaceTexture)
        }

        override fun onSurfaceTextureSizeChanged(
            surfaceTexture: SurfaceTexture,
            width: Int,
            height: Int
        ) = Unit

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
            releaseVideo()
            return true
        }

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) = Unit
    }

    private fun prepareVideo(surfaceTexture: SurfaceTexture) {
        releaseVideo()

        val surface = Surface(surfaceTexture)
        videoSurface = surface

        val player = MediaPlayer()
        mediaPlayer = player

        try {
            requireContext().assets.openFd("test_video.mp4").use { asset ->
                player.setDataSource(asset.fileDescriptor, asset.startOffset, asset.length)
            }
            player.setSurface(surface)
            player.isLooping = true
            player.setOnPreparedListener {
                _binding?.videoStatusText?.text = "Video loaded from assets/test_video.mp4"
                it.start()
            }
            player.setOnErrorListener { _, _, _ ->
                _binding?.videoStatusText?.text = "Video load error"
                true
            }
            player.prepareAsync()
        } catch (e: Exception) {
            _binding?.videoStatusText?.text = "Video load error: ${e.localizedMessage ?: "Unknown"}"
            player.release()
            if (mediaPlayer === player) {
                mediaPlayer = null
            }
            if (videoSurface === surface) {
                videoSurface = null
            }
            surface.release()
        }
    }

    private fun requestCameraPermission() {
        val permission = Manifest.permission.CAMERA
        if (hasCameraPermission()) {
            startCameraPreview()
        } else {
            cameraPermissionLauncher.launch(permission)
        }
    }

    private fun updateCameraStatus() {
        binding.cameraStatusText.text =
            if (hasCameraPermission()) {
                "Camera permission: granted"
            } else {
                "Tap the preview to request camera permission"
            }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraPreview() {
        val context = requireContext()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                try {
                    val currentBinding = _binding ?: return@addListener
                    val provider = cameraProviderFuture.get()
                    cameraProvider = provider

                    val preview = Preview.Builder()
                        .build()
                        .also {
                            it.surfaceProvider = currentBinding.previewView.surfaceProvider
                        }

                    provider.unbindAll()
                    provider.bindToLifecycle(
                        viewLifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview
                    )
                    _binding?.cameraStatusText?.text = "Camera preview: active"
                } catch (e: Exception) {
                    _binding?.cameraStatusText?.text =
                        "Camera preview error: ${e.localizedMessage ?: "Unknown"}"
                }
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    private fun releaseVideo() {
        mediaPlayer?.release()
        mediaPlayer = null
        videoSurface?.release()
        videoSurface = null
    }

    override fun onDestroyView() {
        cameraProvider?.unbindAll()
        cameraProvider = null
        releaseVideo()
        super.onDestroyView()
        _binding = null
    }
}
