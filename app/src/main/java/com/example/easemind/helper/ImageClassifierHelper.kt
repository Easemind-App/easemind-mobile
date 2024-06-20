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
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageClassifierHelper(
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: FloatArray?,
            inferenceTime: Long
        )
    }

    private var interpreter: Interpreter? = null

    init {
        setupInterpreter()
    }

    private fun setupInterpreter() {
        try {
            val model = FileUtil.loadMappedFile(context, "model.tflite")
            val options = Interpreter.Options()
            options.setNumThreads(4)
            interpreter = Interpreter(model, options)
        } catch (e: Exception) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri, context: Context) {
        if (interpreter == null) {
            setupInterpreter()
        }

        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri).copy(Bitmap.Config.ARGB_8888, true)
        }

        bitmap?.let {
            Log.d(TAG, "Original Bitmap Config: ${it.config}, Width: ${it.width}, Height: ${it.height}")

            // Step 1: Convert the bitmap to grayscale format
            val grayscaleBitmap = convertToGrayscale(it)

            Log.d(TAG, "Converted Bitmap Config: ${grayscaleBitmap.config}, Width: ${grayscaleBitmap.width}, Height: ${grayscaleBitmap.height}")

            // Step 2: Resize the grayscale bitmap to 48x48
            val resizedBitmap = Bitmap.createScaledBitmap(grayscaleBitmap, 48, 48, true)

            // Step 3: Convert the resized grayscale bitmap to a float array manually
            val floatArray = convertBitmapToFloatArray(resizedBitmap)

            // Step 4: Prepare the input buffer
            val inputBuffer = ByteBuffer.allocateDirect(4 * 48 * 48 * 1).order(ByteOrder.nativeOrder())
            for (value in floatArray) {
                inputBuffer.putFloat(value)
            }
            inputBuffer.rewind()

            // Step 5: Prepare the output buffer
            val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 48, 48, 1), DataType.FLOAT32)

            // Step 6: Run the model
            var inferenceTime = SystemClock.uptimeMillis()
            interpreter?.run(inputBuffer, outputBuffer.buffer.rewind())
            inferenceTime = SystemClock.uptimeMillis() - inferenceTime

            Log.d(TAG, "Classification result: ${outputBuffer.floatArray.contentToString()}")

            // Return the results
            classifierListener?.onResults(
                outputBuffer.floatArray,
                inferenceTime
            )
        }
    }

    private fun convertToGrayscale(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorMatrixFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorMatrixFilter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return grayscaleBitmap
    }

    private fun convertBitmapToFloatArray(bitmap: Bitmap): FloatArray {
        val width = bitmap.width
        val height = bitmap.height
        val floatArray = FloatArray(width * height)

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        for (i in pixels.indices) {
            val pixel = pixels[i]
            val red = Color.red(pixel)
            floatArray[i] = red / 255.0f
        }
        return floatArray
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
