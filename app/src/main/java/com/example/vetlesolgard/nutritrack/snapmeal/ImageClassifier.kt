package com.example.vetlesolgard.nutritrack.snapmeal

import android.graphics.Bitmap
import android.support.v4.app.FragmentActivity
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ImageClassifier(private val activity: FragmentActivity) {

    private val intValues = IntArray(DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y)
    private var tflite: Interpreter? = null
    private var labelList: List<String> = loadLabelList()

    /** A ByteBuffer to hold image data, to be feed into Tensorflow Lite as inputs.  */
    private var imgData: ByteBuffer?

    private var labelProbArray: Array<FloatArray>? = null
    private var filterLabelProbArray: Array<FloatArray>? = null

    /**
     * Initializes the ImageClassifier
     */
    init {
        labelList = loadLabelList()
        imgData = ByteBuffer.allocateDirect(
            4 * DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE
        )
        imgData!!.order(ByteOrder.nativeOrder())
        labelProbArray = Array(1) { FloatArray(labelList.size) }
        filterLabelProbArray = Array(FILTER_STAGES) { FloatArray(labelList.size) }
        tflite = Interpreter(loadModelFile())
    }

    /** Classifies a frame from the preview stream.  */
    fun classifyFrame(bitmap: Bitmap): String {
        convertBitmapToByteBuffer(bitmap)
        tflite!!.run(imgData!!, labelProbArray!!)

        val sortedLabels = getTopLabels()
        val topValue = sortedLabels.maxBy { it -> it.value }
        return topValue!!.key
    }

    /** Closes tflite to release resources.  */
    fun close() {
        tflite!!.close()
        tflite = null
    }

    /** Reads label list from Assets.  */
    @Throws(IOException::class)
    private fun loadLabelList(): List<String> {
        val labelList = ArrayList<String>()
        val reader = BufferedReader(InputStreamReader(activity.assets.open(LABEL_PATH)))
        var line = reader.readLine()
        while (line != null) {
            labelList.add(line)
            line = reader.readLine()
        }
        reader.close()
        return labelList
    }

    /** Memory-map the model file in Assets.  */
    @Throws(IOException::class)
    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = activity.assets.openFd(MODEL_PATH)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    /** Writes Image data into a `ByteBuffer`.  */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap) {
        if (imgData == null) {
            return
        }
        imgData!!.rewind()
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        // Convert the image to floating point.
        var pixel = 0
        for (i in 0 until DIM_IMG_SIZE_X) {
            for (j in 0 until DIM_IMG_SIZE_Y) {
                val `val` = intValues[pixel++]
                imgData!!.putFloat(((`val` shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData!!.putFloat(((`val` shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData!!.putFloat(((`val` and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
            }
        }
    }

    /** Prints top labels, to be shown in UI as result. */
    private fun getTopLabels(): Map<String, Float> {
        val labelsValueMap = mutableMapOf<String, Float>()
        for (i in labelList.indices) {
            labelsValueMap[labelList[i]] = labelProbArray!![0][i]
        }
        return labelsValueMap
    }

    companion object {
        private const val FILTER_STAGES = 3

        /** Name of the model file stored in Assets.  */
        private const val MODEL_PATH = "graph.lite"

        /** Name of the label file stored in Assets.  */
        private const val LABEL_PATH = "food_labels.txt"

        /** Dimensions of inputs.  */
        private const val DIM_BATCH_SIZE = 1
        private const val DIM_PIXEL_SIZE = 3
        internal const val DIM_IMG_SIZE_X = 224
        internal const val DIM_IMG_SIZE_Y = 224

        private const val IMAGE_MEAN = 128
        private const val IMAGE_STD = 128.0f
    }
}