package com.example.easemind.ui.questionnaire

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.easemind.R
import com.example.easemind.databinding.FragmentFaceRecognationBinding
import com.example.easemind.helper.ImageClassifierHelper
import com.example.storyapp.view.utils.reduceFileImage
import com.example.storyapp.view.utils.uriToFile
import org.tensorflow.lite.task.vision.classifier.Classifications

class FaceRecognationFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cameraBtn.setOnClickListener { startCamera() }
        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.next.setOnClickListener{
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


        // Ensure the view binding is set properly
        binding.next.setOnClickListener {
            currentImageUri?.let {
                Log.d("Image URI", "Image to be analyzed: $it")
                // Add analysis logic here
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

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgQuestion4.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireActivity()).reduceFileImage()
            Log.d("Image Classification File", "showImage: ${imageFile.path}")

            imageClassifierHelper = ImageClassifierHelper(
                context = requireActivity(),
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        showToast(error)
//                        showLoading(false)
                    }

                    override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                        results?.let {
                            moveToResult(it)
                        }
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(uri, requireActivity())
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun moveToResult(results: List<Classifications>) {
        val sortedCategories = results[0].categories.sortedByDescending { it?.score }
        val topResult = sortedCategories.firstOrNull()

        if (topResult != null) {
            val label = topResult.label
            val score = topResult.score ?: 0.0f

            // Simpan hasil klasifikasi ke dalam variabel
            classificationResult = Pair(label, score)

            // Cetak log hasil
            Log.d("Classification Result", "Label: $label, Score: $score")

        } else {
            showToast("No result found")
        }

    }


    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
