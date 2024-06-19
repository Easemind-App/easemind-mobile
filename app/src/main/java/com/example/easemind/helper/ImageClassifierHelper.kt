package com.example.easemind.helper

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.example.easemind.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResults: Int = 3,
    val modelName: String = "model.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }

    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri, context: Context) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(48, 48, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.FLOAT32))
            .build()

        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)

        bitmap?.let {
            // Convert to greyscale
            val greyscaleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(greyscaleBitmap)
            val paint = Paint()
            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0f)
            val filter = ColorMatrixColorFilter(colorMatrix)
            paint.colorFilter = filter
            canvas.drawBitmap(bitmap, 0f, 0f, paint)

            var inferenceTime = SystemClock.uptimeMillis()

            // Convert the greyscale Bitmap to a 1-channel TensorImage
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(greyscaleBitmap)

            // TensorImage's internal storage is now a 1D float array.
            // We need to copy the data to a new buffer with the shape 1 x 48 x 48 x 1
            val greyscaleBuffer = tensorImage.tensorBuffer
            val newBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 48, 48, 1), DataType.FLOAT32)

            val greyscaleArray = greyscaleBuffer.floatArray
            newBuffer.loadArray(greyscaleArray)

            // Update tensorImage with the new buffer
            tensorImage.load(newBuffer)

            val results = imageClassifier?.classify(tensorImage)
            inferenceTime = SystemClock.uptimeMillis() - inferenceTime

            classifierListener?.onResults(
                results,
                inferenceTime
            )
        }
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
