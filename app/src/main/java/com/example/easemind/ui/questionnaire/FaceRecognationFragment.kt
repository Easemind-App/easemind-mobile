package com.example.easemind.ui.questionnaire

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.easemind.R
import com.example.easemind.databinding.FragmentFaceRecognationBinding
import com.example.easemind.helper.ImageClassifierHelper
import com.example.storyapp.view.utils.reduceFileImage
import com.example.storyapp.view.utils.uriToFile
import org.tensorflow.lite.support.label.Category

class FaceRecognationFragment : Fragment(){

    private var currentImageUri: Uri? = null
    private lateinit var binding: FragmentFaceRecognationBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var classificationResult: Pair<String, Float>? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireActivity(), "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireActivity(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(requireActivity(), REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFaceRecognationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.uploadBtn.setOnClickListener { startGallery() }
        binding.cameraBtn.setOnClickListener { startCamera() }
        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.next.setOnClickListener {
            Log.d("FaceRecognationFragment", "Next button clicked")
            analyzeImage()
            val inputFeelingsFragment = InputFeelingsFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.container, inputFeelingsFragment, InputFeelingsFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun startCamera() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgQuestion4.setImageURI(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun analyzeImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireActivity()).reduceFileImage()
            Log.d("Image Classification File", "Image File Path: ${imageFile.path}")

            imageClassifierHelper = ImageClassifierHelper(
                context = requireActivity(),
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        showToast(error)
                    }

                    override fun onResults(results: List<Category>, inferenceTime: Long) {
                        moveToResult(results)
                    }
                }
            )

            Log.d("Image URI", "Starting classification for URI: $uri")
            imageClassifierHelper.classifyStaticImage(uri, requireActivity())
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun moveToResult(results: List<Category>) {
        // Temukan probabilitas tertinggi
        val topResult = results.maxByOrNull { it.score }

        // Simpan hasil klasifikasi ke dalam variabel
        if (topResult != null) {
            classificationResult = Pair(topResult.label, topResult.score)

            // Cetak log hasil
            Log.d("Classification Result", "Label: ${topResult.label}, Score: ${topResult.score}")

        } else {
            Log.d("Classification Result", "No result found")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
